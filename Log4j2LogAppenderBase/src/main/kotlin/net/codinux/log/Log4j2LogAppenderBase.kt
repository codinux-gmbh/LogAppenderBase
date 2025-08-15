package net.codinux.log

import net.dankito.datetime.Instant
import org.apache.logging.log4j.core.LogEvent
import org.apache.logging.log4j.core.appender.AbstractAppender
import org.apache.logging.log4j.core.config.Property

open class Log4j2LogAppenderBase(
    appenderName: String,
    protected open val logWriter: LogWriter
): AbstractAppender(appenderName, null, null, true, Property.EMPTY_ARRAY) {

    protected open val config = logWriter.loggedEventFields

    protected open val eventSupportsInstant: Boolean = try {
        // starting from log4j 2.11 LogEvent has a getInstant() method
        LogEvent::class.java.getDeclaredMethod("getInstant")
        true
    } catch (ignored: Throwable) {
        false
    }


    override fun append(event: LogEvent?) {
        if (logWriter.isEnabled && event != null) {
            logWriter.writeRecord(
                if (eventSupportsInstant) convertInstant(event.instant) else Instant.ofEpochMilli(event.timeMillis),
                event.level.name(),
                event.message.formattedMessage,
                if (config.logsLoggerName) event.loggerName else null,
                if (config.logsThreadName) event.threadName else null,
                if (config.logsException) event.thrown else null,
                if (config.logsMdc) event.contextData.toMap() else null,
                if (config.logsMarker) event.marker?.name else null
            )
        }
    }

    protected open fun convertInstant(instant: org.apache.logging.log4j.core.time.Instant) =
        Instant(instant.epochSecond, instant.nanoOfSecond)

}