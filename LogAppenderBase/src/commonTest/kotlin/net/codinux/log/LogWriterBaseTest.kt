package net.codinux.log

import io.kotest.matchers.collections.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import net.codinux.log.config.LogAppenderConfig
import net.codinux.log.config.WriterConfig
import net.dankito.datetime.Instant
import kotlin.js.JsName
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LogWriterBaseTest {

    companion object {
        private const val SendTimeTolerance = 50L
    }


    @Test
    @JsName("writeRecord_async_Records_get_written_with_delay")
    fun `writeRecord async - Records get written with delay`() = runTest {
        val testRecord = listOf(createRecord())
        val sendPeriod = 50L

        val writtenRecords = writeRecords(sendPeriod, testRecord)

        val receivedWriteRecordsEvents = assertWrittenRecords(testRecord, writtenRecords)
        receivedWriteRecordsEvents.shouldHaveSize(1)
    }


    @Test
    @JsName("writeRecords_async_Records_get_written_with_delay")
    fun `writeRecords async - Records get written with delay`() = runTest {
        val testRecords = IntRange(1, 5).map { createRecord("Record #$it") }
        val sendPeriod = 50L

        val writtenRecords = writeRecords(sendPeriod, testRecords)

        val receivedWriteRecordsEvents = assertWrittenRecords(testRecords, writtenRecords)
        receivedWriteRecordsEvents.shouldHaveAtMostSize(2) // as records are send in batches writeRecords() is called less than 5 times
    }


    @Test
    @JsName("writeRecords_async_Re_add_failed_records")
    fun `writeRecords async - Re-add failed records`() = runTest {
        val testRecords = IntRange(1, 5).map { createRecord("Record #$it") }
        // on first call we say all records with an odd index fail
        val recordsToFailOnFirstWriteCall = testRecords
            .filterIndexed { index, _ -> index % 2 == 0 }
            .onEach { it.mappedRecord = this@LogWriterBaseTest.serializeRecord(it) }
        val sendPeriod = 50L

        val writtenRecords = mutableMapOf<Int, List<String>>()
        var countWriteRecordCalls = 0

        val underTest = object : LogWriterBase<String>(createConfig(sendPeriod)) {

            override fun instantiateMappedRecord() = LogRecord("") // not used by this implementation

            override suspend fun mapRecord(record: LogRecord<String>) {
                record.mappedRecord = this@LogWriterBaseTest.serializeRecord(record)
            }

            override suspend fun writeRecords(records: List<LogRecord<String>>): List<LogRecord<String>> {
                writtenRecords[countWriteRecordCalls++] = records.map { it.mappedRecord } // make a copy, releasing records modifies list if records fail

                return if ((countWriteRecordCalls == 1 && records.size == testRecords.size) || // on first call we say writing some of the records fails
                    (countWriteRecordCalls == 2 && records != recordsToFailOnFirstWriteCall)) { // sometimes the first call contains only a part of testRecords, then return failed records on second call
                    recordsToFailOnFirstWriteCall
                } else {
                    emptyList()
                }
            }

        }


        testRecords.forEach { record ->
            writeRecord(underTest, record)
        }

        delay(sendPeriod * 3)

        underTest.close()


        writtenRecords.size.shouldBeIn(2, 3) // as on first call some records failed there has to be another call with the failed records

        // the original 5 records may get send in different chunks, not all in one chunks
        val originalRecords = if (writtenRecords.size == 2) writtenRecords[0]!!
                                else writtenRecords[0]!! + writtenRecords[1]!!
        originalRecords.shouldContainExactlyInAnyOrder(testRecords.map { serializeRecord(it) })

        val failedRecords = if (writtenRecords.size == 2) writtenRecords[1]!! else writtenRecords[2]!!
        failedRecords.shouldHaveSize(3)
        failedRecords.shouldContainExactlyInAnyOrder(serializeRecord(testRecords[0]), serializeRecord(testRecords[2]), serializeRecord(testRecords[4]))
    }


    private fun assertWrittenRecords(sendRecords: List<LogRecord<String>>, writtenRecords: List<WrittenRecord>): List<WrittenRecord> {
        val writtenLogRecords = writtenRecords.flatMap { it.records }

        sendRecords.shouldHaveSize(writtenLogRecords.size)

        writtenLogRecords.shouldContainAll(sendRecords.map { serializeRecord(it) })

        return writtenRecords
    }

    private suspend fun writeRecords(sendPeriod: Long = 50L, records: List<LogRecord<String>>): List<WrittenRecord> {
        val config = createConfig(sendPeriod)
        val writtenRecords = mutableListOf<WrittenRecord>()

        val underTest = object : LogWriterBase<String>(config) {

            override fun instantiateMappedRecord() = LogRecord("") // not used by this implementation

            override suspend fun mapRecord(record: LogRecord<String>) {
                record.mappedRecord = this@LogWriterBaseTest.serializeRecord(record)
            }

            override suspend fun writeRecords(records: List<LogRecord<String>>): List<LogRecord<String>> {
                writtenRecords.add(WrittenRecord(now(), records.map { it.mappedRecord }))

                return emptyList()
            }

        }

        records.forEach { record ->
            writeRecord(underTest, record)
        }

        delay(sendPeriod + SendTimeTolerance)

        underTest.close()

        return writtenRecords
    }

    private fun writeRecord(writer: LogWriterBase<String>, record: LogRecord<String>) {
        writer.writeRecord(record.timestamp, record.level,
            record.message, record.loggerName, record.threadName, record.exception, record.mdc, record.marker, record.ndc)
    }

    private fun serializeRecord(record: LogRecord<String>) =
        serializeRecord(record.timestamp, record.level, record.message,
            record.loggerName, record.threadName, record.exception, record.mdc, record.marker, record.ndc)

    private fun serializeRecord(
        timestamp: Instant,
        level: String,
        message: String,
        loggerName: String?,
        threadName: String?,
        exception: Throwable?,
        mdc: Map<String, String>?,
        marker: String?,
        ndc: String?
    ) = "$timestamp $level $loggerName [$threadName] $message"

    private fun createConfig(sendPeriod: Long) =
        LogAppenderConfig(writer = WriterConfig(sendLogRecordsPeriodMillis = sendPeriod))

    private fun createRecord(message: String = "Test message"): LogRecord<String> {
        return LogRecord(message)
    }

    private fun now() = Instant.now()

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