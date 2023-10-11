package net.codinux.log.statelogger

import org.slf4j.LoggerFactory

open class LogbackStateLogger(loggerName: String) : AppenderStateLogger { // TODO: use ContextAware

    constructor(loggerClass: Class<*>) : this(loggerClass.name)


    protected open val log = LoggerFactory.getLogger(loggerName)

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