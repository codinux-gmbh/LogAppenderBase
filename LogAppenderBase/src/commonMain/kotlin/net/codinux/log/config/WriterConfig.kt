package net.codinux.log.config

open class WriterConfig(
    open var hostUrl: String = HostUrlNotSet,

    open var username: String? = UsernameNotSet,
    open var password: String? = PasswordNotSet,

    open var maxBufferedLogRecords: Int = MaxBufferedLogRecordsDefaultValue,
    open var maxLogRecordsPerBatch: Int = MaxLogRecordsPerBatchDefaultValue,
    open var sendLogRecordsPeriodMillis: Long = SendLogRecordsPeriodMillisDefaultValue,

    open var connectTimeoutMillis: Long? = ConnectTimeoutNotSet,
    open var requestTimeoutMillis: Long? = RequestTimeoutNotSet
) {

    companion object {

        const val HostUrlNotSet = "null"

        val UsernameNotSet: String? = null
        const val UsernameNotSetString = "null"

        val PasswordNotSet: String? = null
        const val PasswordNotSetString = "null"

        const val MaxBufferedLogRecordsDefaultValue = 5000
        const val MaxLogRecordsPerBatchDefaultValue = 100
        const val SendLogRecordsPeriodMillisDefaultValue = 100L

        val ConnectTimeoutNotSet: Long? = null

        val RequestTimeoutNotSet: Long? = null

    }
}