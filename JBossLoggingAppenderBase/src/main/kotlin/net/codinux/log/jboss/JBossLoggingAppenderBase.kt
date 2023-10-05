package net.codinux.log.jboss

import kotlinx.datetime.toKotlinInstant
import net.codinux.log.LogWriter
import org.jboss.logmanager.ExtFormatter
import org.jboss.logmanager.ExtHandler
import org.jboss.logmanager.ExtLogRecord

open class JBossLoggingAppenderBase(
    protected open val isAppenderEnabled: Boolean,
    protected open val logWriter: LogWriter
) : ExtHandler() {

    protected open val config = logWriter.config.fields

    init {
        formatter = object : ExtFormatter() {
            override fun format(record: ExtLogRecord): String {
                return formatMessage(record)
            }
        }
    }


    override fun doPublish(record: ExtLogRecord?) {
        if (isAppenderEnabled && record != null) {
            val message = formatter.formatMessage(record)
            val ndc = if (logWriter.config.fields.logsNdc && record.ndc.isNullOrBlank() == false) record.ndc else null

            logWriter.writeRecord(
                record.instant.toKotlinInstant(),
                record.level.name,
                message,
                if (config.logsLoggerName) record.loggerName else null,
                if (config.logsThreadName) record.threadName else null,
                if (config.logsException) record.thrown else null,
                if (config.logsMdc) record.mdcCopy else null,
                if (config.logsMarker) record.marker?.toString() else null,
                ndc
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