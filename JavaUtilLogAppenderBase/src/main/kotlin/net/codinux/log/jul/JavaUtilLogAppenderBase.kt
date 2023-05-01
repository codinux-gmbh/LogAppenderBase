package net.codinux.log.jul

import net.codinux.log.LogWriter
import net.codinux.log.extensions.microAndNanosecondsPart
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

            logWriter.writeRecord(record.instant.toEpochMilli(), record.instant.microAndNanosecondsPart, record.level.name,
                message, record.loggerName, threadName, record.thrown)
        }
    }

    override fun flush() {
        logWriter.flush()
    }

    override fun close() {
        logWriter.close()
    }
}