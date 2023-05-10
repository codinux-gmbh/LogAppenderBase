package net.codinux.log

import kotlinx.datetime.Instant

interface LogWriter {

    /**
     * Previously we used a LogRecord object instead of all of these parameters directly but to get it (almost) allocation and therefore
     * Garbage Collection free according to these design principles https://github.com/real-logic/aeron/wiki/Design-Principles,
     * we removed instantiating a LogRecord and Instant object for each log event, even though the API has been
     * nicer, better readable and better maintainable before.
     */
    fun writeRecord(
        timestamp: Instant,
        level: String,
        message: String,
        loggerName: String,
        threadName: String,
        exception: Throwable? = null,
        mdc: Map<String, String>? = null,
        marker: String? = null,
        ndc: String? = null
    )

    fun flush()

    fun close()

}