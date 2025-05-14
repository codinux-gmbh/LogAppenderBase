package net.codinux.log.statelogger

import kotlin.time.Duration

interface AppenderStateLogger {

    fun debug(message: String)

    fun info(message: String)

    fun warn(message: String)

    fun error(message: String, e: Throwable? = null)

    fun error(message: String, e: Throwable?, logAtMaximumEach: Duration, category: String = message, addDurationToLogMessage: Boolean = true)

}