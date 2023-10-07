package net.codinux.log.config


open class LogAppenderConfig(
    open var enabled: Boolean = EnabledDefaultValue,

    open var hostUrl: String = HostUrlNotSet,

    open var username: String? = UsernameNotSet,
    open var password: String? = PasswordNotSet,

    open var fields: LogAppenderFieldsConfig = LogAppenderFieldsConfig(),

    open var maxBufferedLogRecords: Int = MaxBufferedLogRecordsDefaultValue,
    open var maxLogRecordsPerBatch: Int = MaxLogRecordsPerBatchDefaultValue,
    open var sendLogRecordsPeriodMillis: Long = SendLogRecordsPeriodMillisDefaultValue
) {

    companion object {

        const val EnabledDefaultValue = true
        const val EnabledDefaultValueString = EnabledDefaultValue.toString()

        const val HostUrlNotSet = "null"

        val UsernameNotSet: String? = null
        const val UsernameNotSetString = "null"

        val PasswordNotSet: String? = null
        const val PasswordNotSetString = "null"

        const val MaxBufferedLogRecordsDefaultValue = 5000
        const val MaxLogRecordsPerBatchDefaultValue = 100
        const val SendLogRecordsPeriodMillisDefaultValue = 100L

    }

}