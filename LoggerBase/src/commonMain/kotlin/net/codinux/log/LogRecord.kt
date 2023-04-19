package net.codinux.log

import kotlinx.datetime.Instant
import kotlin.jvm.JvmOverloads

open class LogRecord @JvmOverloads constructor(
    open val message: String,

    open val timestamp: Instant,

    open val level: String,

    open val loggerName: String,

    open val threadName: String,

    open val exception: Throwable? = null,

    open val host: String? = null,

    open var mdc: Map<String, String>? = null,

    open val marker: String? = null,

    open val ndc: String? = null
) {

    override fun toString(): String {
        return "$timestamp $level $message"
    }

}