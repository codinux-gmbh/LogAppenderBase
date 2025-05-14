package net.codinux.log.statelogger

import org.jboss.logmanager.Level
import org.jboss.logmanager.Logger

open class JBossLoggingStateLogger(loggerName: String) : AppenderStateLoggerBase(), AppenderStateLogger {

    constructor(loggerClass: Class<*>) : this(loggerClass.name)


    protected open val log = Logger.getLogger(loggerName)


    override fun debug(message: String) {
        log.fine(message)
    }

    override fun info(message: String) {
        log.info(message)
    }

    override fun warn(message: String) {
        log.warning(message)
    }

    override fun error(message: String, e: Throwable?) {
        log.log(Level.ERROR, message, e)
    }

}