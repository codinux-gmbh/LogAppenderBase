package net.codinux.log


open class LogAppenderConfig(
    open var enabled: Boolean = EnabledDefaultValue,

    open var host: String = HostNotSetValue,

    open var username: String? = UsernameDefaultValue,
    open var password: String? = PasswordDefaultValue,

    open var messageFieldName: String = MessageDefaultFieldName,

    open var includeLogLevel: Boolean = IncludeLogLevelDefaultValue,
    open var logLevelFieldName: String = LogLevelDefaultFieldName,

    open var includeLoggerName: Boolean = IncludeLoggerNameDefaultValue,
    open var loggerNameFieldName: String = LoggerNameDefaultFieldName,

    open var includeLoggerClassName: Boolean = IncludeLoggerClassNameDefaultValue,
    open var loggerClassNameFieldName: String = LoggerClassNameDefaultFieldName,

    open var includeThreadName: Boolean = IncludeThreadNameDefaultValue,
    open var threadNameFieldName: String = ThreadNameDefaultFieldName,

    open var includeHost: Boolean = IncludeHostDefaultValue,
    open var hostFieldName: String = HostDefaultFieldName,

    open var deviceName: String? = DeviceNameDefaultValue,
    open var includeDeviceName: Boolean = IncludeDeviceNameDefaultValue,
    open var deviceNameFieldName: String = DeviceNameDefaultFieldName,

    open var appName: String? = AppNameDefaultValue,
    open var includeAppName: Boolean = IncludeAppNameDefaultValue,
    open var appNameFieldName: String = AppNameDefaultFieldName,

    open var includeStacktrace: Boolean = IncludeStacktraceDefaultValue,
    open var stacktraceFieldName: String = StacktraceDefaultFieldName,
    open var stacktraceMaxFieldLength: Int = StacktraceMaxFieldLengthDefaultValue,

    open var includeMdc: Boolean = IncludeMdcDefaultValue,
    open var mdcKeysPrefix: String? = MdcFieldsPrefixDefaultValue,

    open var includeMarker: Boolean = IncludeMarkerDefaultValue,
    open var markerFieldName: String = MarkerDefaultFieldName,

    open var includeNdc: Boolean = IncludeNdcDefaultValue,
    open var ndcFieldName: String = NdcDefaultFieldName,

    open var includeKubernetesInfo: Boolean = IncludeKubernetesInfoDefaultValue,
    open var kubernetesFieldsPrefix: String? = KubernetesFieldsPrefixDefaultValue,

    open var includeKubernetesLabels: Boolean = IncludeKubernetesLabelsDefaultValue,
    open var kubernetesLabelsPrefix: String? = KubernetesLabelsPrefixDefaultValue,

    open var includeKubernetesAnnotations: Boolean = IncludeKubernetesAnnotationsDefaultValue,
    open var kubernetesAnnotationsPrefix: String? = KubernetesAnnotationsPrefixDefaultValue,

    open var maxLogRecordsPerBatch: Int = MaxLogRecordsPerBatchDefaultValue,
    open var maxBufferedLogRecords: Int = MaxBufferedLogRecordsDefaultValue,
    open var sendLogRecordsPeriodMillis: Long = SendLogRecordsPeriodMillisDefaultValue
) {

    companion object {
        
        const val True= true
        const val TrueString= "true"
        const val False= false
        const val FalseString= "false"

        const val EnabledDefaultValue= True
        const val EnabledDefaultValueString= TrueString

        const val HostNotSetValue = "null"

        val UsernameDefaultValue: String? = null
        const val UsernameDefaultValueString = "null"

        val PasswordDefaultValue: String? = null
        const val PasswordDefaultValueString = "null"

        const val MessageDefaultFieldName = "message"

        const val IncludeLogLevelDefaultValue= True
        const val IncludeLogLevelDefaultValueString= TrueString
        const val LogLevelDefaultFieldName = "level"

        const val IncludeLoggerNameDefaultValue= True
        const val IncludeLoggerNameDefaultValueString= TrueString
        const val LoggerNameDefaultFieldName = "logger"

        const val IncludeLoggerClassNameDefaultValue= False
        const val IncludeLoggerClassNameDefaultValueString= FalseString
        const val LoggerClassNameDefaultFieldName = "loggerClass"

        const val IncludeThreadNameDefaultValue= True
        const val IncludeThreadNameDefaultValueString= TrueString
        const val ThreadNameDefaultFieldName = "thread"

        const val IncludeHostDefaultValue= True
        const val IncludeHostDefaultValueString= TrueString
        const val HostDefaultFieldName = "host"

        val DeviceNameDefaultValue: String? = null
        const val DeviceNameDefaultValueString = "null"
        const val IncludeDeviceNameDefaultValue= False
        const val IncludeDeviceNameDefaultValueString= FalseString
        const val DeviceNameDefaultFieldName = "device"

        val AppNameDefaultValue: String? = null
        const val AppNameDefaultValueString = "null"
        const val IncludeAppNameDefaultValue= True
        const val IncludeAppNameDefaultValueString= TrueString
        const val AppNameDefaultFieldName = "app"

        const val IncludeStacktraceDefaultValue= True
        const val IncludeStacktraceDefaultValueString= TrueString
        const val StacktraceDefaultFieldName = "stacktrace"
        const val StacktraceMaxFieldLengthDefaultValue = 32766 - 100 // subtract a little buffer
        const val StacktraceMaxFieldLengthDefaultValueString = StacktraceMaxFieldLengthDefaultValue.toString()

        const val IncludeMdcDefaultValue= True
        const val IncludeMdcDefaultValueString= TrueString
        const val MdcFieldsPrefixDefaultValue: String = "mdc"

        const val IncludeMarkerDefaultValue= False
        const val IncludeMarkerDefaultValueString= FalseString
        const val MarkerDefaultFieldName: String = "marker"

        const val IncludeNdcDefaultValue= False
        const val IncludeNdcDefaultValueString= FalseString
        const val NdcDefaultFieldName: String = "ndc"

        const val IncludeKubernetesInfoDefaultValue= False
        const val IncludeKubernetesInfoDefaultValueString= FalseString
        const val KubernetesFieldsPrefixDefaultValue: String = "k8s"

        const val IncludeKubernetesLabelsDefaultValue= False
        const val IncludeKubernetesLabelsDefaultValueString= FalseString
        const val KubernetesLabelsPrefixDefaultValue: String = "label"

        const val IncludeKubernetesAnnotationsDefaultValue= False
        const val IncludeKubernetesAnnotationsDefaultValueString= FalseString
        const val KubernetesAnnotationsPrefixDefaultValue: String = "annotation"

        const val MaxLogRecordsPerBatchDefaultValue = 100
        const val MaxBufferedLogRecordsDefaultValue = 2000
        const val SendLogRecordsPeriodMillisDefaultValue = 100L

    }

}