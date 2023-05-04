package net.codinux.log

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.toList
import net.codinux.log.data.*
import net.codinux.log.kubernetes.*
import net.codinux.log.statelogger.AppenderStateLogger
import net.codinux.log.statelogger.StdOutStateLogger

@OptIn(ExperimentalCoroutinesApi::class)
abstract class LogWriterBase(
    protected open val config: LogAppenderConfig,
    protected open val stateLogger: AppenderStateLogger = StdOutStateLogger(),
    protected open val processData: ProcessData = ProcessDataRetriever(stateLogger).retrieveProcessData()
) : LogWriter {

    protected abstract fun serializeRecord(
        timestampMillisSinceEpoch: Long,
        timestampNanoOfMillisecond: Long?,
        level: String,
        message: String,
        loggerName: String,
        threadName: String,
        exception: Throwable?,
        mdc: Map<String, String>?,
        marker: String?,
        ndc: String?
    ): String

    protected abstract suspend fun writeRecords(records: List<String>): List<String>


    private val recordsToWrite = Channel<String>(config.maxBufferedLogRecords, BufferOverflow.DROP_OLDEST) {
        stateLogger.warn("Message queue is full, dropped one log record. Either increase queue size (via config parameter maxBufferedLogRecords) " +
                "or the count log records to write per batch (maxLogRecordsPerBatch) or decrease the period to write logs (sendLogRecordsPeriodMillis).")
    }

    private val senderScope = CoroutineScope(Dispatchers.IOorDefault)

    private val receiverScope = CoroutineScope(Dispatchers.IOorDefault)

    protected open var podInfo: PodInfo? = null

    init {
        receiverScope.async {
            if (config.includeKubernetesInfo) {
                KubernetesInfoRetrieverRegistry.init(stateLogger)
                podInfo = KubernetesInfoRetrieverRegistry.Registry.retrieveCurrentPodInfo()
            }

            val writeLogRecordsPeriodMillis = if (config.appendLogsAsync) config.sendLogRecordsPeriodMillis
                                            else 5L
            asyncWriteLoop(writeLogRecordsPeriodMillis)
        }
    }

    override fun writeRecord(
        timestampMillisSinceEpoch: Long,
        timestampNanoOfMillisecond: Long?,
        level: String,
        message: String,
        loggerName: String,
        threadName: String,
        exception: Throwable?,
        mdc: Map<String, String>?,
        marker: String?,
        ndc: String?
    ) {
        // as writeRecords() is a suspend function even if config.appendLogsAsync == false we cannot write log record synchronously
        // (if we don't want to call runBlocking { } on each log event), therefore also add these to recordsToWrite queue
        senderScope.async {
            try {
                recordsToWrite.send(serializeRecord(timestampMillisSinceEpoch, timestampNanoOfMillisecond, level, message,
                    loggerName, threadName, exception, mdc, marker, ndc))
            } catch (e: Throwable) {
                if (e !is CancellationException) {
                    stateLogger.error("Could not write log record '$timestampMillisSinceEpoch $level $message'", e)
                }
            }
        }
    }


    protected open suspend fun asyncWriteLoop(writeLogRecordsPeriodMillis: Long) {
        var failedRecords: List<String> = emptyList()

        while (senderScope.isActive && receiverScope.isActive) { // may find a better signal
            try {
                val nextBatch = failedRecords.toMutableList()

                while (recordsToWrite.isNotEmpty && recordsToWrite.isClosedForReceive == false && nextBatch.size < config.maxLogRecordsPerBatch) {
                    nextBatch.add(recordsToWrite.receive())
                }

                if (nextBatch.isNotEmpty()) {
                    failedRecords = writeRecords(nextBatch)
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
            senderScope.cancelSafely()

            flush()

            receiverScope.cancelSafely()

            recordsToWrite.close()
        } catch (e: Throwable) {
            stateLogger.error("Closing LogWriter failed", e)
        }
    }

    private val <T> Channel<T>.isNotEmpty
        get() = isEmpty == false

    protected open fun CoroutineScope.cancelSafely() {
        try {
            if (this.isActive) {
                this.cancel()
            }
        } catch (e: Throwable) {
            stateLogger.error("Could not cancel CoroutineScope", e)
        }
    }

}