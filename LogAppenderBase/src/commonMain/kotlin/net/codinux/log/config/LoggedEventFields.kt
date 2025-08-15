package net.codinux.log.config

interface LoggedEventFields {

    val logsLoggerName: Boolean

    val logsThreadName: Boolean

    val logsException: Boolean

    val logsMdc: Boolean

    val logsMarker: Boolean

    val logsNdc: Boolean


    companion object {
        val None: LoggedEventFields = object : LoggedEventFields {
            override val logsLoggerName: Boolean = false
            override val logsThreadName: Boolean = false
            override val logsException: Boolean = false
            override val logsMdc: Boolean = false
            override val logsMarker: Boolean = false
            override val logsNdc: Boolean = false
        }
    }

}