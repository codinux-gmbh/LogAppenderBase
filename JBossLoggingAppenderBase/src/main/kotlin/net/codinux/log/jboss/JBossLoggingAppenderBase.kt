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
            val ndc = if (record.ndc.isNullOrBlank()) null else record.ndc

            logWriter.writeRecord(record.instant.toKotlinInstant(), record.level.name,
                message, record.loggerName, record.threadName, record.thrown, record.mdcCopy, record.marker?.toString(), ndc)
        }
    }

    override fun flush() {
        logWriter.flush()
    }

    override fun close() {
        logWriter.close()
    }
}