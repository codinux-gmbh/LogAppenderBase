package net.codinux.log

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.toList
import kotlinx.datetime.Instant
import net.codinux.log.config.LogAppenderConfig
import net.codinux.log.data.*
import net.codinux.log.extensions.cancelSafely
import net.codinux.log.extensions.isNotEmpty
import net.codinux.log.kubernetes.*
import net.codinux.log.statelogger.AppenderStateLogger
import net.codinux.log.statelogger.StdOutStateLogger
import kotlin.math.min

abstract class LogWriterBase<T>(
    override val config: LogAppenderConfig,
    override val stateLogger: AppenderStateLogger = StdOutStateLogger(),
    protected open val processData: ProcessData = ProcessDataRetriever(stateLogger).retrieveProcessData(),
    protected open val mapper: LogRecordMapper = LogRecordMapper(config.fields)
) : LogWriter {

    protected abstract fun instantiateMappedRecord(): T

    protected abstract suspend fun mapRecord(
        timestamp: Instant,
        level: String,
        message: String,
        loggerName: String?,
        threadName: String?,
        exception: Throwable?,
        mdc: Map<String, String>?,
        marker: String?,
        ndc: String?
    ): T

    protected abstract suspend fun writeRecords(records: List<T>): List<T>


    protected open val writerConfig = config.writer

    protected open val cachedMappedRecords = Channel<T>(writerConfig.maxBufferedLogRecords)

    protected open val recordsToWrite = Channel<T>(writerConfig.maxBufferedLogRecords, BufferOverflow.DROP_OLDEST) {
        stateLogger.warn("Message queue is full, dropped one log record. Either increase queue size (via config parameter maxBufferedLogRecords) " +
                "or the count log records to write per batch (maxLogRecordsPerBatch) or decrease the period to write logs (sendLogRecordsPeriodMillis).")
    }

    protected open val senderScope = CoroutineScope(Dispatchers.IOorDefault)

    protected open val receiverScope = CoroutineScope(Dispatchers.IOorDefault)

    protected open var podInfo: PodInfo? = null

    protected open var isFullyInitialized = false // TODO: this is not thread safe / volatile

    var countSentRecords: Long = 0
        protected set

    init {
        initializeWriter()
    }

    protected open fun initializeWriter() {
        mapper.processData = processData

        receiverScope.async {
            if (config.fields.includeKubernetesInfo) {
                retrievePodInfo()
            }

            Platform.addShutdownHook {
                close()
            }

            isFullyInitialized = true

            // pre-cache mapped record objects
            IntRange(0, min(1_000, writerConfig.maxBufferedLogRecords / 2)).forEach {
                cachedMappedRecords.send(instantiateMappedRecord())
            }

            asyncWriteLoop(writerConfig.sendLogRecordsPeriodMillis)
        }
    }

    protected open suspend fun retrievePodInfo() {
        try {
            KubernetesInfoRetrieverRegistry.init(stateLogger)

            podInfo = KubernetesInfoRetrieverRegistry.Registry.retrieveCurrentPodInfo()
            mapper.podInfo = podInfo
        } catch (e: Throwable) {
            // TODO: add a retry if retrieving Pod info fails?
            stateLogger.error("Could not retrieve Pod info from Kubernetes API server", e)
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
                recordsToWrite.send(mapRecord(timestamp, level, message, loggerName, threadName, exception, mdc, marker, ndc))
            } catch (e: Throwable) {
                if (e !is CancellationException) {
                    stateLogger.error("Could not write log record '$timestamp $level $message'", e)
                }
            }
        }
    }

    protected open suspend fun getMappedRecordObject(): T {
        // if writer is not fully initialized (e.g. PodInfo hasn't been retrieved yet), then wait till it is initialized and pre-allocates mapped record objects
        return if (cachedMappedRecords.isNotEmpty || isFullyInitialized == false) {
            cachedMappedRecords.receive()
        } else {
            instantiateMappedRecord()
        }
    }

    protected open suspend fun releaseMappedRecords(records: List<T>) {
        records.forEach {
            cachedMappedRecords.send(it)
        }
    }


    protected open suspend fun asyncWriteLoop(writeLogRecordsPeriodMillis: Long) {
        var failedRecords: List<T> = emptyList()

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

    private suspend fun asyncWriteLoopStopped(failedRecords: List<T>) {
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