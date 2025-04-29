package net.codinux.log.statelogger

import net.dankito.datetime.Instant
import kotlin.time.Duration

abstract class AppenderStateLoggerBase : AppenderStateLogger {

    protected open val lastErrorCalls = mutableMapOf<String, Instant>() // TODO: use thread safe Map


    override fun error(message: String, e: Throwable?, logAtMaximumEach: Duration, category: String, addDurationToLogMessage: Boolean) {
        val lastErrorCall = lastErrorCalls[category]

        if (lastErrorCall == null || minimumTimeElapsed(lastErrorCall, logAtMaximumEach)) {
            lastErrorCalls[category] = Instant.now()

            if (addDurationToLogMessage) {
                // TODO: later convert Duration to a nicer string, e.g. "5m" -> "5 min"
                error("$message This message is logged only once every $logAtMaximumEach.", e)
            } else {
                error(message, e)
            }
        }
    }

    protected open fun minimumTimeElapsed(lastErrorCall: Instant, logAtMaximumEach: Duration): Boolean {
        val now = Instant.now()

        // yes, milliseconds would be more correct, but saves same calculation time to simply use property epochSeconds
//        return lastErrorCall.toEpochMilliseconds() + logAtMaximumEach.inWholeMilliseconds < now.toEpochMilliseconds()
        return lastErrorCall.epochSeconds + logAtMaximumEach.inWholeSeconds < now.epochSeconds
    }

}