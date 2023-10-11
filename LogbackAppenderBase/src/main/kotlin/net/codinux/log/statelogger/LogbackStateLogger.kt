package net.codinux.log.statelogger

import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class LogbackStateLogger(
    protected open val log: Logger
) : AppenderStateLogger { // TODO: use ContextAware

    constructor(loggerName: String) : this(LoggerFactory.getLogger(loggerName))

    constructor(loggerClass: Class<*>) : this(LoggerFactory.getLogger(loggerClass))


    override fun info(message: String) {
        log.info(message)
    }

    override fun warn(message: String) {
        log.warn(message)
    }

    override fun error(message: String, e: Throwable?) {
        log.error(message, e)
    }

}