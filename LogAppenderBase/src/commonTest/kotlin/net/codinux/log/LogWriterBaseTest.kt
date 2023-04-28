package net.codinux.log

import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldHaveAtMostSize
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.comparables.shouldBeLessThan
import kotlinx.atomicfu.AtomicArray
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.atomicArrayOfNulls
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LogWriterBaseTest {

    companion object {
        private const val SendTimeTolerance = 50L
    }


    @Test
    fun `writeRecord sync - Records get written almost immediately`() = runTest {
        val testRecord = listOf(createRecord())
        val writtenRecords = atomicArrayOfNulls<WrittenRecord>(10)

        writeRecordsSync(writtenRecords, testRecord)

        val receivedWriteRecordsEvents = assertWrittenRecords(testRecord, writtenRecords)
        receivedWriteRecordsEvents.shouldHaveSize(1)
        (now() - receivedWriteRecordsEvents.first().writeTimestamp).inWholeMilliseconds.shouldBeLessThan(SendTimeTolerance)
    }

    @Test
    fun `writeRecord async - Records get written with delay`() = runTest {
        val testRecord = listOf(createRecord())
        val sendPeriod = 50L
        val writtenRecords = atomicArrayOfNulls<WrittenRecord>(10)

        writeRecordsAsync(sendPeriod, writtenRecords, testRecord)

        val receivedWriteRecordsEvents = assertWrittenRecords(testRecord, writtenRecords)
        receivedWriteRecordsEvents.shouldHaveSize(1)
        (now() - receivedWriteRecordsEvents.first().writeTimestamp).inWholeMilliseconds.shouldBeLessThan(sendPeriod)
    }


    private fun assertWrittenRecords(sendRecords: List<LogRecord>, writtenRecordsArray: AtomicArray<WrittenRecord?>): List<WrittenRecord> {
        val writtenRecords = (0 until writtenRecordsArray.size)
            .mapNotNull { index -> writtenRecordsArray[index].value }
        val writtenLogRecords = writtenRecords.flatMap { it.records }

        sendRecords.shouldHaveSize(writtenLogRecords.size)

        writtenLogRecords.shouldContainAll(sendRecords)

        return writtenRecords
    }

    private suspend fun writeRecordsAsync(sendPeriod: Long = 50L, writtenRecords: AtomicArray<WrittenRecord?>, records: List<LogRecord>) {
        writeRecords(true, sendPeriod, writtenRecords, records)
    }

    private suspend fun writeRecordsSync(writtenRecords: AtomicArray<WrittenRecord?>, records: List<LogRecord>) {
        writeRecords(false, 0L, writtenRecords, records)
    }

    private suspend fun writeRecords(writeAsync: Boolean, sendPeriod: Long = 50L, writtenRecords: AtomicArray<WrittenRecord?>, records: List<LogRecord>) {
        val config = LogAppenderConfig(appendLogsAsync = writeAsync, sendLogRecordsPeriodMillis = sendPeriod)
        val countWrittenRecords = atomic(0)

        val underTest = object : LogWriterBase(config) {

            override suspend fun writeRecords(records: List<LogRecord>): List<LogRecord> {
                writtenRecords.get(countWrittenRecords.getAndIncrement()).value = WrittenRecord(now(), records)
                return emptyList()
            }

        }

        records.forEach { record ->
            underTest.writeRecord(record)
        }

        if (writeAsync) {
            delay(sendPeriod * 2)
        } else {
            delay(sendPeriod + SendTimeTolerance)
        }

        underTest.close()
    }

    private fun createRecord() = LogRecord("Test message", now(), "INFO", "", "")

    private fun now() = Clock.System.now()


    data class WrittenRecord(
        val writeTimestamp: Instant,
        val records: List<LogRecord>
    )

}