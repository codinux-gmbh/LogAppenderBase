package net.codinux.log

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.Appender
import org.slf4j.ILoggerFactory


fun LoggerContext.getRootLogger(): Logger =
    this.getLogger(Logger.ROOT_LOGGER_NAME)


// i cannot add extensions methods to org.slf4j.LoggerFactory as it's a complete static Java class

val ILoggerFactory.logbackContext: LoggerContext?
    get() = this as? LoggerContext

fun ILoggerFactory.getRootLogger(): Logger? =
    this.logbackContext?.getRootLogger()

fun ILoggerFactory.addAppenderToRootLogger(appender: Appender<ILoggingEvent>) {
    getRootLogger()?.addAppender(appender)
}