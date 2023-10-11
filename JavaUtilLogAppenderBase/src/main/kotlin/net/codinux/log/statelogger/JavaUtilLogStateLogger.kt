package net.codinux.log.statelogger

import java.util.logging.Level
import java.util.logging.Logger

open class JavaUtilLogStateLogger(loggerName: String) : AppenderStateLogger {

    constructor(loggerClass: Class<*>) : this(loggerClass.name)


    protected open val log = Logger.getLogger(loggerName)

    override fun info(message: String) {
        log.info(message)
    }

    override fun warn(message: String) {
        log.warning(message)
    }

    override fun error(message: String, e: Throwable?) {
        log.log(Level.SEVERE, message, e)
    }

}