package net.codinux.log.config

open class LogAppenderFieldsConfig(
    open var includeLogLevel: Boolean = IncludeLogLevelDefaultValue,
    open var logLevelFieldName: String = LogLevelDefaultFieldName,

    open var includeLoggerName: Boolean = IncludeLoggerNameDefaultValue,
    open var loggerNameFieldName: String = LoggerNameDefaultFieldName,

    open var includeLoggerClassName: Boolean = IncludeLoggerClassNameDefaultValue,
    open var loggerClassNameFieldName: String = LoggerClassNameDefaultFieldName,

    open var includeThreadName: Boolean = IncludeThreadNameDefaultValue,
    open var threadNameFieldName: String = ThreadNameDefaultFieldName,

    open var includeAppName: Boolean = IncludeAppNameDefaultValue,
    open var appNameFieldName: String = AppNameDefaultFieldName,
    open var appName: String? = AppNameDefaultValue,

    open var includeAppVersion: Boolean = IncludeAppVersionDefaultValue,
    open var appVersionFieldName: String = AppVersionDefaultFieldName,
    open var appVersion: String? = AppVersionDefaultValue,

    open var includeJobName: Boolean = IncludeJobNameDefaultValue,
    open var jobNameFieldName: String = JobNameDefaultFieldName,
    open var jobName: String? = JobNameDefaultValue,

    open var includeHostName: Boolean = IncludeHostNameDefaultValue,
    open var hostNameFieldName: String = HostNameDefaultFieldName,

    open var includeHostIp: Boolean = IncludeHostIpDefaultValue,
    open var hostIpFieldName: String = HostIpDefaultFieldName,

    // TODO: add exceptionClass field?

    open var includeStacktrace: Boolean = IncludeStacktraceDefaultValue,
    open var stacktraceFieldName: String = StacktraceDefaultFieldName,
    open var stacktraceMaxFieldLength: Int = StacktraceMaxFieldLengthDefaultValue,

    open var includeMdc: Boolean = IncludeMdcDefaultValue,
    open var mdcKeysPrefix: String? = MdcFieldsPrefixDefaultValue,

    // TODO: how to handle multiple Marker (slf4j 2.x)?

    open var includeMarker: Boolean = IncludeMarkerDefaultValue,
    open var markerFieldName: String = MarkerDefaultFieldName,

    open var includeNdc: Boolean = IncludeNdcDefaultValue,
    open var ndcFieldName: String = NdcDefaultFieldName,

    open var includeKubernetesInfo: Boolean = IncludeKubernetesInfoDefaultValue,
    open var kubernetesFieldsPrefix: String? = KubernetesFieldsPrefixDefaultValue,

    open var kubernetesFields: KubernetesFieldsConfig = KubernetesFieldsConfig(),

) {

    open val logsLoggerName: Boolean
        get() = includeLoggerName || includeLoggerClassName

    open val logsThreadName: Boolean
        get() = includeThreadName

    open val logsException: Boolean
        get() = includeStacktrace

    open val logsMdc: Boolean
        get() = includeMdc

    open val logsMarker: Boolean
        get() = includeMarker

    open val logsNdc: Boolean
        get() = includeNdc


    companion object {

        private const val True = true
        private const val False = false
        

        const val MessageDefaultFieldName = "message"

        const val IncludeLogLevelDefaultValue = True
        const val IncludeLogLevelDefaultValueString = IncludeLogLevelDefaultValue.toString()
        const val LogLevelDefaultFieldName = "level"

        const val IncludeLoggerNameDefaultValue = True
        const val IncludeLoggerNameDefaultValueString = IncludeLoggerNameDefaultValue.toString()
        const val LoggerNameDefaultFieldName = "logger"

        const val IncludeLoggerClassNameDefaultValue = False
        const val IncludeLoggerClassNameDefaultValueString = IncludeLoggerClassNameDefaultValue.toString()
        const val LoggerClassNameDefaultFieldName = "loggerClass"

        const val IncludeThreadNameDefaultValue = True
        const val IncludeThreadNameDefaultValueString = IncludeThreadNameDefaultValue.toString()
        const val ThreadNameDefaultFieldName = "thread"

        val AppNameDefaultValue: String? = null
        const val AppNameDefaultValueString = "null"
        const val IncludeAppNameDefaultValue = True
        const val IncludeAppNameDefaultValueString = IncludeAppNameDefaultValue.toString()
        const val AppNameDefaultFieldName = "app"

        val AppVersionDefaultValue: String? = null
        const val AppVersionDefaultValueString = "null"
        const val IncludeAppVersionDefaultValue = False
        const val IncludeAppVersionDefaultValueString = IncludeAppVersionDefaultValue.toString()
        const val AppVersionDefaultFieldName = "version"

        val JobNameDefaultValue: String? = null
        const val JobNameDefaultValueString = "null"
        const val IncludeJobNameDefaultValue = False
        const val IncludeJobNameDefaultValueString = IncludeJobNameDefaultValue.toString()
        const val JobNameDefaultFieldName = "job"

        const val IncludeHostNameDefaultValue = False
        const val IncludeHostNameDefaultValueString = IncludeHostNameDefaultValue.toString()
        const val HostNameDefaultFieldName = "host"

        const val IncludeHostIpDefaultValue = False
        const val IncludeHostIpDefaultValueString = IncludeHostIpDefaultValue.toString()
        const val HostIpDefaultFieldName = "hostIP"

        const val IncludeStacktraceDefaultValue = True
        const val IncludeStacktraceDefaultValueString = IncludeStacktraceDefaultValue.toString()
        const val StacktraceDefaultFieldName = "stacktrace"
        const val StacktraceMaxFieldLengthDefaultValue = 32766 - 100 // subtract a little buffer
        const val StacktraceMaxFieldLengthDefaultValueString = StacktraceMaxFieldLengthDefaultValue.toString()

        const val IncludeMdcDefaultValue = True
        const val IncludeMdcDefaultValueString = IncludeMdcDefaultValue.toString()
        const val MdcFieldsPrefixDefaultValue: String = "mdc"

        const val IncludeMarkerDefaultValue = False
        const val IncludeMarkerDefaultValueString = IncludeMarkerDefaultValue.toString()
        const val MarkerDefaultFieldName: String = "marker"

        const val IncludeNdcDefaultValue = False
        const val IncludeNdcDefaultValueString = IncludeNdcDefaultValue.toString()
        const val NdcDefaultFieldName: String = "ndc"

        const val IncludeKubernetesInfoDefaultValue = False
        const val IncludeKubernetesInfoDefaultValueString = IncludeKubernetesInfoDefaultValue.toString()
        const val KubernetesFieldsPrefixDefaultValue: String = "k8s"

    }

}