package net.codinux.log.config

open class WriterConfig(
    open var maxBufferedLogRecords: Int = MaxBufferedLogRecordsDefaultValue,
    open var maxLogRecordsPerBatch: Int = MaxLogRecordsPerBatchDefaultValue,
    open var sendLogRecordsPeriodMillis: Long = SendLogRecordsPeriodMillisDefaultValue,

    open var connectTimeoutMillis: Long? = ConnectTimeoutNotSet,
    open var requestTimeoutMillis: Long? = RequestTimeoutNotSet
) {

    companion object {

        const val MaxBufferedLogRecordsDefaultValue = 25000
        const val MaxLogRecordsPerBatchDefaultValue = 250
        const val SendLogRecordsPeriodMillisDefaultValue = 40L

        val ConnectTimeoutNotSet: Long? = null

        val RequestTimeoutNotSet: Long? = null

    }
}