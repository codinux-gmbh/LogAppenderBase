package net.codinux.log.statelogger

import net.codinux.kotlin.concurrent.atomic.AtomicInt
import net.codinux.kotlin.concurrent.collections.ConcurrentMap
import net.dankito.datetime.Instant
import kotlin.time.Duration

abstract class AppenderStateLoggerBase : AppenderStateLogger {

    protected open val lastErrorCalls = ConcurrentMap<String, Instant>()

    protected open val countSuppressedCalls = ConcurrentMap<String, AtomicInt>()


    override fun error(message: String, e: Throwable?, logAtMaximumEach: Duration, category: String, addDurationToLogMessage: Boolean) {
        val lastErrorCall = lastErrorCalls[category]

        if (lastErrorCall == null || minimumTimeElapsed(lastErrorCall, logAtMaximumEach)) {
            lastErrorCalls[category] = getCurrentTime()
            val suppressedCalls = countSuppressedCalls[category]?.get()
            countSuppressedCalls[category] = AtomicInt(0)

            if (addDurationToLogMessage) {
                // TODO: later convert Duration to a nicer string, e.g. "5m" -> "5 min"
                error("$message${if (message.endsWith('.')) "" else "."} This message is logged only once every $logAtMaximumEach" +
                        (if (suppressedCalls == null) "." else " and has been suppressed $suppressedCalls times during that time."), e)
            } else {
                error(message, e)
            }
        } else {
            countSuppressedCalls[category]?.incrementAndGet()
        }
    }

    protected open fun minimumTimeElapsed(lastErrorCall: Instant, logAtMaximumEach: Duration): Boolean {
        val now = getCurrentTime()

        return lastErrorCall.toEpochMilliseconds() + logAtMaximumEach.inWholeMilliseconds < now.toEpochMilliseconds()
    }

    // so that it's overridable in unit tests
    protected open fun getCurrentTime() = Instant.now()

}