package net.codinux.log

import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldHaveAtMostSize
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.maps.shouldHaveSize
import kotlinx.atomicfu.AtomicArray
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.atomicArrayOfNulls
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.js.JsName
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LogWriterBaseTest {

    companion object {
        private const val SendTimeTolerance = 50L
    }


    @Test
    @JsName("writeRecord_sync_Records_get_written_almost_immediately")
    fun `writeRecord sync - Records get written almost immediately`() = runTest {
        val testRecord = listOf(createRecord())
        val writtenRecords = atomicArrayOfNulls<WrittenRecord>(10)

        writeRecordsSync(writtenRecords, testRecord)

        val receivedWriteRecordsEvents = assertWrittenRecords(testRecord, writtenRecords)
        receivedWriteRecordsEvents.shouldHaveSize(1)
    }

    @Test
    @JsName("writeRecord_async_Records_get_written_with_delay")
    fun `writeRecord async - Records get written with delay`() = runTest {
        val testRecord = listOf(createRecord())
        val sendPeriod = 50L
        val writtenRecords = atomicArrayOfNulls<WrittenRecord>(10)

        writeRecordsAsync(sendPeriod, writtenRecords, testRecord)

        val receivedWriteRecordsEvents = assertWrittenRecords(testRecord, writtenRecords)
        receivedWriteRecordsEvents.shouldHaveSize(1)
    }


    @Test
    @JsName("writeRecords_sync_Records_get_written_almost_immediately")
    fun `writeRecords sync - Records get written almost immediately`() = runTest {
        val testRecords = IntRange(1, 5).map { createRecord("Record #$it") }
        val writtenRecords = atomicArrayOfNulls<WrittenRecord>(10)

        writeRecordsSync(writtenRecords, testRecords)

        val receivedWriteRecordsEvents = assertWrittenRecords(testRecords, writtenRecords)
        receivedWriteRecordsEvents.shouldHaveAtMostSize(2) // as records are send in batches writeRecords() is called less than 5 times
    }

    @Test
    @JsName("writeRecords_async_Records_get_written_with_delay")
    fun `writeRecords async - Records get written with delay`() = runTest {
        val testRecords = IntRange(1, 5).map { createRecord("Record #$it") }
        val sendPeriod = 50L
        val writtenRecords = atomicArrayOfNulls<WrittenRecord>(10)

        writeRecordsAsync(sendPeriod, writtenRecords, testRecords)

        val receivedWriteRecordsEvents = assertWrittenRecords(testRecords, writtenRecords)
        receivedWriteRecordsEvents.shouldHaveAtMostSize(2) // as records are send in batches writeRecords() is called less than 5 times
    }


    @Test
    @JsName("writeRecords_async_Re_add_failed_records")
    fun `writeRecords async - Re-add failed records`() = runTest {
        val testRecords = IntRange(1, 5).map { createRecord("Record #$it") }
        val sendPeriod = 50L

        val writtenRecords = mutableMapOf<Int, List<String>>()
        val countWriteRecordCalls = atomic(0)

        val underTest = object : LogWriterBase(createConfig(true, sendPeriod)) {

            override fun serializeRecord(
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
            ): String =
                this@LogWriterBaseTest.serializeRecord(timestampMillisSinceEpoch, timestampMicroAndNanosecondsPart, level,
                    message, loggerName, threadName, exception, mdc, marker, ndc)

            override suspend fun writeRecords(records: List<String>): List<String> {
                writtenRecords[countWriteRecordCalls.getAndIncrement()] = records

                // on first call we say all records with an odd index fail
                return if (countWriteRecordCalls.value == 1) {
                    records.filterIndexed { index, _ -> index % 2 == 0 }
                } else {
                    emptyList()
                }
            }

        }


        testRecords.forEach { record ->
            writeRecord(underTest, record)
        }

        delay(sendPeriod * 2)

        underTest.close()


        writtenRecords.shouldHaveSize(2) // as on first call some records failed there has to be another call with the failed records

        writtenRecords[0]!!.shouldContainAll(testRecords.map { serializeRecord(it) })

        val failedRecords = writtenRecords[1]!!
        failedRecords.shouldHaveSize(3)
        failedRecords.shouldContainAll(serializeRecord(testRecords[0]), serializeRecord(testRecords[2]), serializeRecord(testRecords[4]))
    }


    private fun assertWrittenRecords(sendRecords: List<LogRecord>, writtenRecordsArray: AtomicArray<WrittenRecord?>): List<WrittenRecord> {
        val writtenRecords = (0 until writtenRecordsArray.size)
            .mapNotNull { index -> writtenRecordsArray[index].value }
        val writtenLogRecords = writtenRecords.flatMap { it.records }

        sendRecords.shouldHaveSize(writtenLogRecords.size)

        writtenLogRecords.shouldContainAll(sendRecords.map { serializeRecord(it) })

        return writtenRecords
    }

    private suspend fun writeRecordsAsync(sendPeriod: Long = 50L, writtenRecords: AtomicArray<WrittenRecord?>, records: List<LogRecord>) {
        writeRecords(true, sendPeriod, writtenRecords, records)
    }

    private suspend fun writeRecordsSync(writtenRecords: AtomicArray<WrittenRecord?>, records: List<LogRecord>) {
        writeRecords(false, 0L, writtenRecords, records)
    }

    private suspend fun writeRecords(writeAsync: Boolean, sendPeriod: Long = 50L, writtenRecords: AtomicArray<WrittenRecord?>, records: List<LogRecord>) {
        val config = createConfig(writeAsync, sendPeriod)
        val countWriteRecordCalls = atomic(0)

        val underTest = object : LogWriterBase(config) {

            override fun serializeRecord(
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
            ): String =
                this@LogWriterBaseTest.serializeRecord(timestampMillisSinceEpoch, timestampMicroAndNanosecondsPart, level,
                    message, loggerName, threadName, exception, mdc, marker, ndc)

            override suspend fun writeRecords(records: List<String>): List<String> {
                writtenRecords[countWriteRecordCalls.getAndIncrement()].value = WrittenRecord(now(), records)

                return emptyList()
            }

        }

        records.forEach { record ->
            writeRecord(underTest, record)
        }

        val waitForCompletion = if (writeAsync) sendPeriod else 0
        delay(waitForCompletion + SendTimeTolerance)

        underTest.close()
    }

    private fun writeRecord(writer: LogWriterBase, record: LogRecord) {
        writer.writeRecord(record.timestampMillisSinceEpoch, record.timestampMicroAndNanosecondsPart, record.level,
            record.message, record.loggerName, record.threadName, record.exception, record.mdc, record.marker, record.ndc)
    }

    private fun serializeRecord(record: LogRecord) =
        serializeRecord(record.timestampMillisSinceEpoch, record.timestampMicroAndNanosecondsPart, record.level, record.message,
            record.loggerName, record.threadName, record.exception, record.mdc, record.marker, record.ndc)

    private fun serializeRecord(
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
    ) = "$timestampMillisSinceEpoch $level $loggerName [$threadName] $message"

    private fun createConfig(writeAsync: Boolean, sendPeriod: Long) =
        LogAppenderConfig(appendLogsAsync = writeAsync, sendLogRecordsPeriodMillis = sendPeriod)

    private fun createRecord(message: String = "Test message"): LogRecord {
        val now = now()

        return LogRecord(message, now.toEpochMilliseconds(), now.nanosecondsOfSecond % 1_000_000L, "INFO", "", "")
    }

    private fun now() = Clock.System.now()

    private suspend fun delay(millis: Long) {
        withContext(Dispatchers.Default) { // use Dispatchers.Default as otherwise delay() calls will be ignored
            kotlinx.coroutines.delay(millis)
        }
    }


    data class WrittenRecord(
        val writeTimestamp: Instant,
        val records: List<String>
    )

}