package net.codinux.log

import net.dankito.datetime.toKmpInstant
import java.util.logging.Handler

open class JavaUtilLogAppenderBase(
    protected open val logWriter: LogWriter
) : Handler() {

    protected open val config = logWriter.loggedEventFields

    override fun publish(record: java.util.logging.LogRecord?) {
        if (logWriter.isEnabled && record != null && isLoggable(record)) {
            var message = if (record.message == null) "" else record.message
            val threadName = Thread.currentThread().name

            if (record.parameters != null) {
                message = String.format(record.message, *record.parameters)
            }

            logWriter.writeRecord(
                record.instant.toKmpInstant(),
                record.level.name,
                message,
                if (config.logsLoggerName) record.loggerName else null,
                if (config.logsThreadName) threadName else null,
                if (config.logsException) record.thrown else null
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