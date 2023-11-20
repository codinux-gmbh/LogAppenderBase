package net.codinux.log

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class LogRecord<T>(
    var mappedRecord: T
) {

    var timestamp: Instant = Clock.System.now()

    var level: String = ""

    var message: String = ""

    var loggerName: String? = null

    var threadName: String? = null

    var exception: Throwable? = null

    var mdc: Map<String, String>? = null

    var marker: String? = null

    var ndc: String? = null

    var dynamicFields: MutableList<String> = mutableListOf()

}