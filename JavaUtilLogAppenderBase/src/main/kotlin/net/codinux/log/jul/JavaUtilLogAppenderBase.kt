package net.codinux.log.jul

import kotlinx.datetime.toKotlinInstant
import net.codinux.log.LogWriter
import java.util.logging.Handler

open class JavaUtilLogAppenderBase(
    protected open val isAppenderEnabled: Boolean,
    protected open val logWriter: LogWriter
) : Handler() {

    override fun publish(record: java.util.logging.LogRecord?) {
        if (isAppenderEnabled && record != null && isLoggable(record)) {
            var message = if (record.message == null) "" else record.message
            val threadName = Thread.currentThread().name

            if (record.parameters != null) {
                message = String.format(record.message, *record.parameters)
            }

            logWriter.writeRecord(
                record.instant.toKotlinInstant(),
                record.level.name,
                message,
                if (logWriter.config.logsLoggerName) record.loggerName else null,
                if (logWriter.config.logsThreadName) threadName else null,
                if (logWriter.config.logsException) record.thrown else null
            )
        }
    }

    override fun flush() {
        logWriter.flush()
    }

    override fun close() {
        logWriter.close()
    }
}