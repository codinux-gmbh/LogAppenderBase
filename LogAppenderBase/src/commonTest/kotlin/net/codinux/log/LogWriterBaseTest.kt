package net.codinux.log

import kotlinx.atomicfu.AtomicArray
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.atomicArrayOfNulls
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LogWriterBaseTest {

    companion object {
        private const val SendTimeTolerance = 50L
    }


    @Test
    fun `writeRecord sync - Records get written almost immediately`() = runTest {
        val testRecord = createRecord()
        val writtenRecords = atomicArrayOfNulls<WrittenRecord>(10)

        writeRecordsSync(writtenRecords, testRecord)

        assertWrittenRecords(listOf(testRecord), writtenRecords)
        assertTrue((now() - writtenRecords[0].value!!.writeTimestamp).inWholeMilliseconds < SendTimeTolerance)
    }

    @Test
    fun `writeRecord async - Records get written with delay`() = runTest {
        val testRecord = createRecord()
        val sendPeriod = 50L
        val writtenRecords = atomicArrayOfNulls<WrittenRecord>(10)

        writeRecordsAsync(sendPeriod, writtenRecords, testRecord)

        assertWrittenRecords(listOf(testRecord), writtenRecords)
        assertTrue((now() - writtenRecords[0].value!!.writeTimestamp).inWholeMilliseconds < sendPeriod)
    }


    private fun assertWrittenRecords(sendRecords: List<LogRecord>, writtenRecordsArray: AtomicArray<WrittenRecord?>): List<WrittenRecord> {
        val writtenRecords = (0 until writtenRecordsArray.size)
            .mapNotNull { index -> writtenRecordsArray[index].value }
        val writtenLogRecords = writtenRecords.flatMap { it.records }

        assertEquals(sendRecords.size, writtenLogRecords.size)

        assertTrue(writtenLogRecords.containsAll(sendRecords))

        return writtenRecords
    }

    private suspend fun writeRecordsAsync(sendPeriod: Long = 50L, writtenRecords: AtomicArray<WrittenRecord?>, vararg records: LogRecord) {
        writeRecords(true, sendPeriod, writtenRecords, *records)
    }

    private suspend fun writeRecordsSync(writtenRecords: AtomicArray<WrittenRecord?>, vararg records: LogRecord) {
        writeRecords(false, 0L, writtenRecords, *records)
    }

    private suspend fun writeRecords(writeAsync: Boolean, sendPeriod: Long = 50L, writtenRecords: AtomicArray<WrittenRecord?>, vararg records: LogRecord) {
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