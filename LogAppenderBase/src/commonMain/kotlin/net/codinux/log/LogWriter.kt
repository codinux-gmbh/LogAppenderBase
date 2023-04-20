package net.codinux.log

interface LogWriter {

    fun writeRecord(record: LogRecord)

    fun close()

}