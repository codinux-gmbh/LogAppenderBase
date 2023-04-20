package net.codinux.log.jul

import kotlinx.datetime.toKotlinInstant
import net.codinux.log.LogRecord
import net.codinux.log.LogWriter
import java.net.InetAddress
import java.util.logging.Handler

open class JavaUtilLogAppenderBase(
    protected open val isAppenderEnabled: Boolean,
    protected open val logWriter: LogWriter
) : Handler() {

    protected open val hostName: String? = InetAddress.getLocalHost().hostName

    override fun publish(record: java.util.logging.LogRecord?) {
        if (isAppenderEnabled && record != null && isLoggable(record)) {
            logWriter.writeRecord(mapRecord(record))
        }
    }

    protected open fun mapRecord(record: java.util.logging.LogRecord): LogRecord {
        var message = if (record.message == null) "" else record.message
        val threadName = Thread.currentThread().name

        if (record.parameters != null) {
            message = String.format(record.message, *record.parameters)
        }

        return LogRecord(message, record.instant.toKotlinInstant(), record.level.name, record.loggerName,
            threadName, record.thrown, hostName)
    }

    override fun flush() {
        logWriter.flush()
    }

    override fun close() {
        logWriter.close()
    }
}