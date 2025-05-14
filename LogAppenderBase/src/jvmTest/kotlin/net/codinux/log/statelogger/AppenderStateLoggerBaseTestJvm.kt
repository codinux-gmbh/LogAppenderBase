package net.codinux.log.statelogger

import net.dankito.datetime.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds

class AppenderStateLoggerBaseTestJvm {

    private val errorLogs = mutableMapOf<Instant, String>()

    private val underTest = object : AppenderStateLoggerBase() {
        override fun debug(message: String) { }

        override fun info(message: String) { }

        override fun warn(message: String) { }

        override fun error(message: String, e: Throwable?) {
            errorLogs[Instant.now()] = message
        }
    }


    @BeforeTest
    fun setUp() {
        errorLogs.clear()
    }


    @Test
    fun assertAfterLogAtMaximumEachMessageGetsLoggedAgain() {
        val category = "TestCategory"
        val logAtMaximumEach = 100.milliseconds
        val addDurationToLogMessage = false

        underTest.error("Message 1", null, logAtMaximumEach, category, addDurationToLogMessage)

        // test in commonTest with delay() and advanceTimeBy() didn't work, doesn't sleep or advance time, so test always fails
        Thread.sleep(101)

        underTest.error("Last Message", null, logAtMaximumEach, category, addDurationToLogMessage)

        assertTrue(errorLogs.size == 2)
        assertContains(errorLogs.values, "Message 1", "Last Message")
    }

}