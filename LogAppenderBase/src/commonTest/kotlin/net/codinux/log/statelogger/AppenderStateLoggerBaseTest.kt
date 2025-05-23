package net.codinux.log.statelogger

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import net.dankito.datetime.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalCoroutinesApi::class)
class AppenderStateLoggerBaseTest {

    private val errorLogs = mutableMapOf<Instant, String>()

    private var virtualizedTime = 0L

    private val underTest = object : AppenderStateLoggerBase() {
        override fun debug(message: String) { }

        override fun info(message: String) { }

        override fun warn(message: String) { }

        override fun error(message: String, e: Throwable?) {
            errorLogs[Instant.now()] = message
        }

        override fun getCurrentTime() = Instant.ofEpochMilli(virtualizedTime)
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

    @Test
    fun assertCountSuppressedCallsGetAddedIfAddDurationToLogMessageIsTrue() = runTest {
        val category = "TestCategory"
        val logAtMaximumEach = 250.milliseconds
        val addDurationToLogMessage = true

        underTest.error("Message 1.", null, logAtMaximumEach, category, addDurationToLogMessage)

        (2..10_000).forEach {
            underTest.error("Message $it", null, logAtMaximumEach, category, addDurationToLogMessage)
        }

        delay(500)
        virtualizedTime = currentTime

        underTest.error("Message 10.001.", null, logAtMaximumEach, category, addDurationToLogMessage)

        assertTrue(errorLogs.size == 2)
        assertContains(errorLogs.values,
            "Message 1. This message is logged only once every 250ms.",
            "Message 10.001. This message is logged only once every 250ms and has been suppressed 9999 times during that time."
        )
    }

}