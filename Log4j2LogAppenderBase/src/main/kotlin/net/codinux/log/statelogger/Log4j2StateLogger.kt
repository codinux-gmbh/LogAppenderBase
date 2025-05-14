package net.codinux.log.statelogger

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

open class Log4j2StateLogger protected constructor(
    protected open val log: Logger
) : AppenderStateLoggerBase(), AppenderStateLogger {

    constructor(loggerName: String) : this(LogManager.getLogger(loggerName))

    constructor(loggerClass: Class<*>) : this(LogManager.getLogger(loggerClass))


    override fun debug(message: String) {
        log.debug(message)
    }

    override fun info(message: String) =
        log.info(message)

    override fun warn(message: String) =
        log.warn(message)

    override fun error(message: String, e: Throwable?) =
        log.error(message, e)

}