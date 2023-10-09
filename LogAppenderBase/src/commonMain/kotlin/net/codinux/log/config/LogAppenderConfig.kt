package net.codinux.log.config


open class LogAppenderConfig(
    open var enabled: Boolean = EnabledDefaultValue,

    open var writer: WriterConfig = WriterConfig(),

    open var fields: LogAppenderFieldsConfig = LogAppenderFieldsConfig(),

    open var stateLoggerName: String? = null
) {

    companion object {

        const val EnabledDefaultValue = true
        const val EnabledDefaultValueString = EnabledDefaultValue.toString()

    }

}