package net.codinux.log.statelogger

import net.dankito.datetime.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.minutes

class AppenderStateLoggerBaseTest {

    private val errorLogs = mutableMapOf<Instant, String>()

    private val underTest = object : AppenderStateLoggerBase() {
        override fun info(message: String) {
        }

        override fun warn(message: String) {
        }

        override fun error(message: String, e: Throwable?) {
            errorLogs[Instant.now()] = message
        }
    }


    @BeforeTest
    fun setUp() {
        errorLogs.clear()
    }


    @Test
    fun assertGetsOnlyLoggedOncePerPeriod() {
        val category = "TestCategory"
        val logAtMaximumEach = 5.minutes
        val addDurationToLogMessage = false

        underTest.error("Message 1", null, logAtMaximumEach, category, addDurationToLogMessage)

        (2..10_000).forEach {
            underTest.error("Message $it", null, logAtMaximumEach, category, addDurationToLogMessage)
        }

        assertTrue(errorLogs.size == 1)
        assertContains(errorLogs.values, "Message 1")
    }

    @Test
    fun assertDurationGetsAddedIfAddDurationToLogMessageIsTrue() {
        val category = "TestCategory"
        val logAtMaximumEach = 5.minutes
        val addDurationToLogMessage = true

        underTest.error("Message 1.", null, logAtMaximumEach, category, addDurationToLogMessage)

        (2..10_000).forEach {
            underTest.error("Message $it", null, logAtMaximumEach, category, addDurationToLogMessage)
        }

        assertTrue(errorLogs.size == 1)
        assertContains(errorLogs.values, "Message 1. This message is logged only once every 5m.")
    }

}