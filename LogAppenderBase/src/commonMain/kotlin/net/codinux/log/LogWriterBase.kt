package net.codinux.log

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.toList
import net.codinux.log.config.LogAppenderConfig
import net.codinux.log.data.*
import net.codinux.log.extensions.cancelSafely
import net.codinux.log.extensions.isNotEmpty
import net.codinux.log.kubernetes.*
import net.codinux.log.statelogger.AppenderStateLogger
import net.codinux.log.statelogger.StdOutStateLogger
import net.dankito.datetime.Instant
import kotlin.math.min
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

abstract class LogWriterBase<T>(
    override val config: LogAppenderConfig,
    override val stateLogger: AppenderStateLogger = StdOutStateLogger(),
    protected open val mapper: LogRecordMapper = LogRecordMapper(config.fields),
    processData: ProcessData? = null,
    protected val logErrorMessagesAtMaximumOncePer: Duration = 5.minutes,
) : LogWriter {

    protected abstract fun instantiateMappedRecord(): LogRecord<T>

    protected abstract suspend fun mapRecord(record: LogRecord<T>)

    protected abstract suspend fun writeRecords(records: List<LogRecord<T>>): List<LogRecord<T>>


    protected open val writerConfig = config.writer

    protected open val cachedMappedRecords = Channel<LogRecord<T>>(writerConfig.maxBufferedLogRecords)

    protected open val recordsToWrite = Channel<LogRecord<T>>(writerConfig.maxBufferedLogRecords, BufferOverflow.DROP_OLDEST) {
        stateLogger.warn("Message queue is full, dropped one log record. Either increase queue size (via config parameter maxBufferedLogRecords) " +
                "or the count log records to write per batch (maxLogRecordsPerBatch) or decrease the period to write logs (sendLogRecordsPeriodMillis).")
    }

    protected open val senderScope = CoroutineScope(Dispatchers.IOorDefault)

    protected open val receiverScope = CoroutineScope(Dispatchers.IOorDefault)

    protected open var isFullyInitialized = false // TODO: this is not thread safe / volatile

    var countSentRecords: Long = 0
        protected set

    init {
        if (config.enabled) {
            initializeWriter(processData)
        }
    }

    protected open fun initializeWriter(processData: ProcessData?) {
        receiverScope.async {
            mapper.processData = processData ?: retrieveProcessData()

            if (config.fields.includeKubernetesInfo) {
                mapper.podInfo = retrievePodInfo()
            }

            PlatformFunctions.addShutdownHook {
                close()
            }

            isFullyInitialized = true

            // pre-cache mapped record objects
            for (i in 0 until min(1_000, writerConfig.maxBufferedLogRecords / 2)) {
                cachedMappedRecords.send(instantiateMappedRecord())
            }

            asyncWriteLoop(writerConfig.sendLogRecordsPeriodMillis)
        }
    }

    protected open suspend fun retrieveProcessData() =
        ProcessDataRetriever(stateLogger).retrieveProcessData()

    protected open suspend fun retrievePodInfo(): PodInfo? {
        return try {
            KubernetesInfoRetrieverRegistry.init(stateLogger)

            KubernetesInfoRetrieverRegistry.Registry.retrieveCurrentPodInfo()
        } catch (e: Throwable) {
            stateLogger.error("Could not retrieve Pod info from Kubernetes API server", e)

            null
        }
    }

    override fun writeRecord(
        timestamp: Instant,
        level: String,
        message: String,
        loggerName: String?,
        threadName: String?,
        exception: Throwable?,
        mdc: Map<String, String>?,
        marker: String?,
        ndc: String?
    ) {
        senderScope.async {
            try {
                val record = getMappedRecordObject().apply {
                    this.timestamp = timestamp
                    this.level = level
                    this.message = message
                    this.loggerName = loggerName
                    this.threadName = threadName
                    this.exception = exception
                    this.mdc = mdc
                    this.marker = marker
                    this.ndc = ndc
                }

                mapRecord(record)

                recordsToWrite.send(record)
            } catch (e: Throwable) {
                if (e !is CancellationException) {
                    stateLogger.error("Could not write log record '$timestamp $level $message'", e,
                        logErrorMessagesAtMaximumOncePer, "Could not write log record")
                }
            }
        }
    }

    protected open suspend fun getMappedRecordObject(): LogRecord<T> {
        // if writer is not fully initialized (e.g. PodInfo hasn't been retrieved yet), then wait till it is initialized and pre-allocates mapped record objects
        return if (cachedMappedRecords.isNotEmpty || isFullyInitialized == false) {
            cachedMappedRecords.receive()
        } else {
            instantiateMappedRecord()
        }
    }

    protected open suspend fun releaseMappedRecords(records: List<LogRecord<T>>) {
        records.forEach {
            cachedMappedRecords.send(it)
        }
    }


    protected open suspend fun asyncWriteLoop(writeLogRecordsPeriodMillis: Long) {
        var failedRecords: List<LogRecord<T>> = emptyList()

        while (senderScope.isActive && receiverScope.isActive) { // may find a better signal
            try {
                val nextBatch = failedRecords.toMutableList()

                while (recordsToWrite.isNotEmpty && recordsToWrite.isClosedForReceive == false && nextBatch.size < writerConfig.maxLogRecordsPerBatch) {
                    nextBatch.add(recordsToWrite.receive())
                }

                if (nextBatch.isNotEmpty()) {
                    failedRecords = writeRecords(nextBatch)

                    countSentRecords += (nextBatch.size - failedRecords.size)

                    if (failedRecords.size != nextBatch.size) { // if all records failed to send there's no need to release these records
                        // in case of failed records this mutates nextBatch, so don't hold a reference to nextBatch
                        nextBatch.removeAll(failedRecords)
                        releaseMappedRecords(nextBatch)
                    }
                }

                delay(writeLogRecordsPeriodMillis)
            } catch (e: Throwable) {
                if (e is CancellationException) {
                    break
                } else {
                    stateLogger.error("Could not write batch", e)
                }
            }
        }

        asyncWriteLoopStopped(failedRecords)
    }

    private suspend fun asyncWriteLoopStopped(failedRecords: List<LogRecord<T>>) {
        stateLogger.info("Stopping asyncWriteLoop()")

        if (failedRecords.isNotEmpty()) {
            writeRecords(failedRecords)
        }

        flushRecords()

        stateLogger.info("asyncWriteLoop() has stopped")
    }

    protected open suspend fun flushRecords() {
        // do not use recordsToWrite.isEmpty, it returns false even though there are still log records to send
        val nextBatch = recordsToWrite.toList()

        if (nextBatch.isNotEmpty()) {
            writeRecords(nextBatch)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun flush() {
        // do not use recordsToWrite.isEmpty, it returns false even though there are still log records to send
        // yes, we really want to use GlobalScope here was we want to assert that Coroutine gets executed before program ends
        GlobalScope.launch {
            flushRecords()
        }
    }

    override fun close() {
        try {
            stateLogger.info("close() called")

            senderScope.cancelSafely()

            flush()

            receiverScope.cancelSafely()

            recordsToWrite.close()
        } catch (e: Throwable) {
            stateLogger.error("Closing LogWriter failed", e)
        }
    }

}