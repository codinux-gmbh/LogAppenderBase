package net.codinux.log

import net.dankito.datetime.toKmpInstant
import org.jboss.logmanager.ExtFormatter
import org.jboss.logmanager.ExtHandler
import org.jboss.logmanager.ExtLogRecord

open class JBossLoggingAppenderBase(
    protected open val logWriter: LogWriter
) : ExtHandler() {

    protected open val config = logWriter.loggedEventFields

    init {
        formatter = object : ExtFormatter() {
            override fun format(record: ExtLogRecord): String {
                return formatMessage(record)
            }
        }
    }


    override fun doPublish(record: ExtLogRecord?) {
        if (logWriter.isEnabled && record != null) {
            val message = formatter.formatMessage(record)
            val ndc = if (config.logsNdc && record.ndc.isNullOrBlank() == false) record.ndc else null

            logWriter.writeRecord(
                record.instant.toKmpInstant(),
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