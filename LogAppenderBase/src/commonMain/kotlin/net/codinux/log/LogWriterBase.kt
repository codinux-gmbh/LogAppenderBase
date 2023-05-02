package net.codinux.log

import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock
import kotlinx.coroutines.*
import net.codinux.log.data.KubernetesInfo
import net.codinux.log.data.KubernetesInfoRetriever
import net.codinux.log.data.ProcessData
import net.codinux.log.data.ProcessDataRetriever
import net.codinux.log.statelogger.AppenderStateLogger
import net.codinux.log.statelogger.StdOutStateLogger

abstract class LogWriterBase(
    protected open val config: LogAppenderConfig,
    protected open val stateLogger: AppenderStateLogger = StdOutStateLogger(),
    protected open val processData: ProcessData = ProcessDataRetriever(stateLogger).retrieveProcessData()
) : LogWriter {

    protected abstract fun serializeRecord(
        timestampMillisSinceEpoch: Long,
        timestampMicroAndNanosecondsPart: Long?,
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


    protected open var kubernetesInfo: KubernetesInfo? = null

    private val recordsToWrite = mutableListOf<String>()

    private val lock = reentrantLock()

    private val isClosed = atomic(false)

    private val coroutineScope = CoroutineScope(Dispatchers.IOorDefault)

    init {
        coroutineScope.async {
            if (config.includeKubernetesInfo) {
                kubernetesInfo = KubernetesInfoRetriever(stateLogger).retrieveKubernetesInfo()
            }

            val writeLogRecordsPeriodMillis = if (config.appendLogsAsync) config.sendLogRecordsPeriodMillis
                                            else 5L
            asyncWriteLoop(writeLogRecordsPeriodMillis)
        }
    }

    override fun writeRecord(
        timestampMillisSinceEpoch: Long,
        timestampMicroAndNanosecondsPart: Long?,
        level: String,
        message: String,
        loggerName: String,
        threadName: String,
        exception: Throwable?,
        mdc: Map<String, String>?,
        marker: String?,
        ndc: String?
    ) {
        lock.withLock {
            // as writeRecords() is a suspend function even if config.appendLogsAsync == false we cannot write log record synchronously
            // (if we don't want to call runBlocking { } on each log event), therefore also add these to recordsToWrite queue
            recordsToWrite.add(serializeRecord(timestampMillisSinceEpoch, timestampMicroAndNanosecondsPart, level, message,
                loggerName, threadName, exception, mdc, marker, ndc))

            if (recordsToWrite.size > config.maxBufferedLogRecords) { // recordsToWrite exceeds max size
                recordsToWrite.removeAt(0) // drop the oldest log record then

                stateLogger.warn("Message queue is full, dropped one log record. Either increase queue size (via config parameter maxBufferedLogRecords) " +
                        "or the count log records to write per batch (maxLogRecordsPerBatch) or decrease the period to write logs (sendLogRecordsPeriodMillis).")
            }
        }
    }


    protected open suspend fun asyncWriteLoop(writeLogRecordsPeriodMillis: Long) {
        while (isClosed.value == false) {
            try {
                val recordsToWrite = lock.withLock {
                    if (recordsToWrite.isEmpty()) emptyList()
                    else calculateRecordsToWrite()
                }

                writeRecordsAndReAddFailedOnes(recordsToWrite)

                delay(writeLogRecordsPeriodMillis)
            } catch (e: Exception) {
                stateLogger.error("Could not write batch", e)
            }
        }

        writeAllRecordsNow()

        stateLogger.info("asyncWriteLoop() has stopped")
    }

    private suspend fun writeRecordsAndReAddFailedOnes(recordsToWrite: List<String>) {
        if (recordsToWrite.isNotEmpty()) {
            val failedRecords = writeRecords(recordsToWrite)

            if (failedRecords.isNotEmpty()) {
                lock.withLock {
                    this.recordsToWrite.addAll(0, failedRecords)
                }
            }
        }
    }

    protected open fun calculateRecordsToWrite(): List<String> {
        val size = recordsToWrite.size

        if (size <= config.maxLogRecordsPerBatch) {
            val recordsToWrite = ArrayList(recordsToWrite)

            this.recordsToWrite.clear()

            return recordsToWrite
        }
        else {
            val fromIndex = size - config.maxLogRecordsPerBatch
            val recordsToWrite = ArrayList(recordsToWrite.subList(fromIndex, size)) // make a copy

            while (this.recordsToWrite.size > fromIndex) {
                this.recordsToWrite.removeAt(fromIndex) // do not call removeAll() as if other records have the same JSON string than all matching strings get removed
            }

            return recordsToWrite
        }
    }

    protected open suspend fun writeAllRecordsNow() {
        if (recordsToWrite.isNotEmpty()) {
            val recordsToWrite = lock.withLock {
                val recordsToWrite = ArrayList(recordsToWrite)

                this.recordsToWrite.clear()

                recordsToWrite
            }

            writeRecordsAndReAddFailedOnes(recordsToWrite)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun flush() {
        if (recordsToWrite.isNotEmpty()) {
            // yes, we really want to use GlobalScope here was we want to assert that Coroutine gets executed before program ends
            GlobalScope.async {
                writeAllRecordsNow()
            }
        }
    }

    override fun close() {
        isClosed.value = true

        flush()

        if (coroutineScope.isActive) {
            coroutineScope.cancel()
        }
    }

}