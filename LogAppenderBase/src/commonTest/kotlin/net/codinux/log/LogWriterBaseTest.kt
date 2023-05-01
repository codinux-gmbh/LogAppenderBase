package net.codinux.log

import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldHaveAtMostSize
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.maps.shouldHaveSize
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


    @Test
    fun `writeRecords sync - Records get written almost immediately`() = runTest {
        val testRecords = IntRange(1, 5).map { createRecord("Record #$it") }
        val writtenRecords = atomicArrayOfNulls<WrittenRecord>(10)

        writeRecordsSync(writtenRecords, testRecords)

        val receivedWriteRecordsEvents = assertWrittenRecords(testRecords, writtenRecords)
        receivedWriteRecordsEvents.shouldHaveAtMostSize(2) // as records are send in batches writeRecords() is called less than 5 times
    }

    @Test
    fun `writeRecords async - Records get written with delay`() = runTest {
        val testRecords = IntRange(1, 5).map { createRecord("Record #$it") }
        val sendPeriod = 50L
        val writtenRecords = atomicArrayOfNulls<WrittenRecord>(10)

        writeRecordsAsync(sendPeriod, writtenRecords, testRecords)

        val receivedWriteRecordsEvents = assertWrittenRecords(testRecords, writtenRecords)
        receivedWriteRecordsEvents.shouldHaveAtMostSize(2) // as records are send in batches writeRecords() is called less than 5 times
    }


    @Test
    fun `writeRecords async - Re-add failed records`() = runTest {
        val testRecords = IntRange(1, 5).map { createRecord("Record #$it") }
        val sendPeriod = 50L

        val writtenRecords = mutableMapOf<Int, List<String>>()
        val countWriteRecordCalls = atomic(0)

        val underTest = object : LogWriterBase(createConfig(true, sendPeriod)) {

            override fun serializeRecord(record: LogRecord): String =
                this@LogWriterBaseTest.serializeRecord(record)

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
            underTest.writeRecord(record)
        }

        delay(sendPeriod * 4)

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
        val countWrittenRecords = atomic(0)

        val underTest = object : LogWriterBase(config) {

            override fun serializeRecord(record: LogRecord): String =
                this@LogWriterBaseTest.serializeRecord(record)

            override suspend fun writeRecords(records: List<String>): List<String> {
                writtenRecords[countWrittenRecords.getAndIncrement()].value = WrittenRecord(now(), records)

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

    private fun serializeRecord(record: LogRecord) = record.toString()

    private fun createConfig(writeAsync: Boolean, sendPeriod: Long) =
        LogAppenderConfig(appendLogsAsync = writeAsync, sendLogRecordsPeriodMillis = sendPeriod)

    private fun createRecord(message: String = "Test message") =
        LogRecord(message, now(), "INFO", "", "")

    private fun now() = Clock.System.now()


    data class WrittenRecord(
        val writeTimestamp: Instant,
        val records: List<String>
    )

}