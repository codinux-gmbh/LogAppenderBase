package net.codinux.log.statelogger

import net.codinux.log.StdErr

open class StdOutStateLogger : AppenderStateLoggerBase(), AppenderStateLogger {

    companion object {
        val Default = StdOutStateLogger()
    }


    override fun debug(message: String) {
        println(message)
    }

    override fun info(message: String) {
        println(message)
    }

    override fun warn(message: String) {
        println(message)
    }

    override fun error(message: String, e: Throwable?) {
        if (e == null) {
            StdErr.println(message)
        } else {
            StdErr.println("${message}: ${e.message}")

            e.printStackTrace()
        }
    }

}