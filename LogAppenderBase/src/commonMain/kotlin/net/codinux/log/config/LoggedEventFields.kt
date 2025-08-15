package net.codinux.log.config

interface LoggedEventFields {

    val logsLoggerName: Boolean

    val logsThreadName: Boolean

    val logsException: Boolean

    val logsMdc: Boolean

    val logsMarker: Boolean

    val logsNdc: Boolean

}