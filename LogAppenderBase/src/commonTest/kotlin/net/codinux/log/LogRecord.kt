package net.codinux.log

import kotlin.jvm.JvmOverloads

open class LogRecord @JvmOverloads constructor(
    open val message: String,

    /**
     * Previously we used kotlinx.datetime.Instant. Even though it makes a nicer API than [timestampMillisSinceEpoch] and [timestampNanoOfMillisecond],
     * we removed it to avoid that an Instant instance has to be created for each log event and to get rid of this external dependency.
     */
    open val timestampMillisSinceEpoch: Long,

    open val timestampNanoOfMillisecond: Long? = null,

    open val level: String,

    open val loggerName: String,

    open val threadName: String,

    open val exception: Throwable? = null,

    open var mdc: Map<String, String>? = null,

    open val marker: String? = null,

    open val ndc: String? = null
) {

    override fun toString(): String {
        return "$timestampMillisSinceEpoch $timestampNanoOfMillisecond $message"
    }

}