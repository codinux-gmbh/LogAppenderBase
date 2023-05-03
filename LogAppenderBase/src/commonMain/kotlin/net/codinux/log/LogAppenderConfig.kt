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

    open var includeHostName: Boolean = IncludeHostNameDefaultValue,
    open var hostNameFieldName: String = HostNameDefaultFieldName,

    open var includeHostIp: Boolean = IncludeHostIpDefaultValue,
    open var hostIpFieldName: String = HostIpDefaultFieldName,

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

    open var appendLogsAsync: Boolean = AppendLogsAsyncDefaultValue,

    open var maxLogRecordsPerBatch: Int = MaxLogRecordsPerBatchDefaultValue,
    open var maxBufferedLogRecords: Int = MaxBufferedLogRecordsDefaultValue,
    open var sendLogRecordsPeriodMillis: Long = SendLogRecordsPeriodMillisDefaultValue
) {

    companion object {
        
        private const val True = true
        private const val False = false


        const val EnabledDefaultValue = True
        const val EnabledDefaultValueString = EnabledDefaultValue.toString()

        const val HostNotSetValue = "null"

        val UsernameDefaultValue: String? = null
        const val UsernameDefaultValueString = "null"

        val PasswordDefaultValue: String? = null
        const val PasswordDefaultValueString = "null"


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

        const val IncludeHostNameDefaultValue = False
        const val IncludeHostNameDefaultValueString = IncludeHostNameDefaultValue.toString()
        const val HostNameDefaultFieldName = "host"

        const val IncludeHostIpDefaultValue = False
        const val IncludeHostIpDefaultValueString = IncludeHostIpDefaultValue.toString()
        const val HostIpDefaultFieldName = "hostIP"

        val DeviceNameDefaultValue: String? = null
        const val DeviceNameDefaultValueString = "null"
        const val IncludeDeviceNameDefaultValue = False
        const val IncludeDeviceNameDefaultValueString = IncludeDeviceNameDefaultValue.toString()
        const val DeviceNameDefaultFieldName = "device"

        val AppNameDefaultValue: String? = null
        const val AppNameDefaultValueString = "null"
        const val IncludeAppNameDefaultValue = False
        const val IncludeAppNameDefaultValueString = IncludeAppNameDefaultValue.toString()
        const val AppNameDefaultFieldName = "app"

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

        const val IncludeKubernetesLabelsDefaultValue = False
        const val IncludeKubernetesLabelsDefaultValueString = IncludeKubernetesLabelsDefaultValue.toString()
        const val KubernetesLabelsPrefixDefaultValue: String = "label"

        const val IncludeKubernetesAnnotationsDefaultValue = False
        const val IncludeKubernetesAnnotationsDefaultValueString = IncludeKubernetesAnnotationsDefaultValue.toString()
        const val KubernetesAnnotationsPrefixDefaultValue: String = "annotation"

        const val AppendLogsAsyncDefaultValue = true

        const val MaxLogRecordsPerBatchDefaultValue = 100
        const val MaxBufferedLogRecordsDefaultValue = 5000
        const val SendLogRecordsPeriodMillisDefaultValue = 100L

    }

}