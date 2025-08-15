package net.codinux.log.config

open class LogAppenderConfig(
    open var enabled: Boolean = EnabledDefaultValue,

    open var hostUrl: String = HostUrlNotSet,

    open var username: String? = UsernameNotSet,
    open var password: String? = PasswordNotSet,

    open var writer: WriterConfig = WriterConfig(),

    open var fields: LogAppenderFieldsConfig = LogAppenderFieldsConfig(),

    open var stateLoggerName: String? = StateLoggerNotSet
) {

    companion object {

        const val EnabledDefaultValue = true
        const val EnabledDefaultValueString = EnabledDefaultValue.toString()

        const val HostUrlNotSet = "null"

        val UsernameNotSet: String? = null
        const val UsernameNotSetString = "null"

        val PasswordNotSet: String? = null
        const val PasswordNotSetString = "null"

        val StateLoggerNotSet: String? = null
        const val StateLoggerNotSetString = "null"

    }

}