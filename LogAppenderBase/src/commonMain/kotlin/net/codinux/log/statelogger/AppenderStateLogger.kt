package net.codinux.log.statelogger

interface AppenderStateLogger {

    fun info(message: String)

    fun warn(message: String)

    fun error(message: String, e: Throwable? = null)

}