package net.codinux.log

import net.codinux.kotlin.concurrent.collections.ConcurrentMap
import net.codinux.log.config.LogAppenderFieldsConfig
import net.codinux.log.data.ProcessData
import net.codinux.log.kubernetes.PodInfo

open class LogRecordMapper(
    protected open val config: LogAppenderFieldsConfig,
    // Loki does not allow null values in Stream / fields
    open var allowNullAsFieldValue: Boolean = true,
    open var escapeControlCharacters: Boolean = true
) {

    open lateinit var processData: ProcessData

    open var podInfo: PodInfo? = null


    protected open val cachedStackTraces = ConcurrentMap<Int?, String>()

    protected open val cachedLoggerClassNames = ConcurrentMap<String, String>()

    /**
     * Add fields that never change during the whole process lifetime
     */
    open fun mapStaticFields(fields: MutableMap<String, String?>) {
        mapField(fields, config.includeHostName, config.hostNameFieldName, processData.hostName)
        mapField(fields, config.includeAppName, config.appNameFieldName, config.appName)
        mapField(fields, config.includeAppVersion, config.appVersionFieldName, config.appVersion)
        mapField(fields, config.includeJobName, config.jobNameFieldName, config.jobName)

        mapPodInfoFields(fields)
    }

    open fun <T> mapLogEventFields(record: LogRecord<T>, fields: MutableMap<String, String?>) {
        mapField(fields, config.includeLogLevel, config.logLevelFieldName, record.level)
        mapField(fields, config.includeLoggerName, config.loggerNameFieldName, record.loggerName)
        mapField(fields, config.includeLoggerClassName, config.loggerClassNameFieldName) { record.loggerName?.let { extractLoggerClassName(it) } }

        mapMdcFields(record, fields, config.includeMdc && record.mdc != null, record.mdc)
        mapDynamicFieldIfNotNull(fields, config.includeMarker, config.markerFieldName, record.marker)
        mapDynamicFieldIfNotNull(fields, config.includeNdc, config.ndcFieldName, record.ndc)
    }

    protected open fun mapField(fields: MutableMap<String, String?>, includeField: Boolean, fieldName: String, valueSupplier: () -> String?) {
        if (includeField) {
            mapField(fields, includeField, fieldName, valueSupplier.invoke())
        }
    }

    protected open fun mapField(fields: MutableMap<String, String?>, includeField: Boolean, fieldName: String, value: String?) {
        if (includeField) {
            // Loki returns 400 Bad request if a stream value is null. Assure that only fields with value != null get added to Stream
            if (value != null || allowNullAsFieldValue) {
                fields[fieldName] = value
            } else {
                fields.remove(fieldName)
            }
        }
    }

    protected open fun mapFieldIfNotNull(fields: MutableMap<String, String?>, includeField: Boolean, fieldName: String, value: String?) {
        mapField(fields, includeField && value != null, fieldName, value)
    }

    protected open fun mapDynamicFieldIfNotNull(fields: MutableMap<String, String?>, includeField: Boolean, fieldName: String, value: String?) {
        if (includeField) {
            if (value.isNullOrEmpty()) {
                fields.remove(fieldName)
            } else {
                fields[fieldName] = value
            }
        }
    }

    protected open fun <T> mapMdcFields(record: LogRecord<T>, fields: MutableMap<String, String?>, includeMdc: Boolean, mdc: Map<String, String>?) {
        if (includeMdc) {
            record.dynamicFields.forEach { name ->
                fields.remove(name)
            }

            if (mdc != null) {
                val prefix = config.mdcKeysPrefix

                mdc.mapNotNull { (key, value) ->
                    val fieldName = prefix + escapeDynamicLabelName(key)
                    record.dynamicFields.add(fieldName)
                    mapField(fields, true, fieldName, value)
                }
            }
        }
    }

    // so that subclasses are able to do some Log storage specific escaping
    protected open fun escapeDynamicLabelName(key: String): String = key

    protected open fun mapPodInfoFields(fields: MutableMap<String, String?>) {
        if (config.includeKubernetesInfo) {
            podInfo?.let { info ->
                val prefix = config.kubernetesFieldsPrefix
                val kubernetesFields = config.kubernetesFields

                mapField(fields, kubernetesFields.includeNamespace, prefix + kubernetesFields.namespaceFieldName, info.namespace)
                mapField(fields, kubernetesFields.includePodName, prefix + kubernetesFields.podNameFieldName, info.podName)
                mapField(fields, kubernetesFields.includePodIp, prefix + kubernetesFields.podIpFieldName, info.podIp)
                mapField(fields, kubernetesFields.includeStartTime, prefix + kubernetesFields.startTimeFieldName, info.startTime)

                mapFieldIfNotNull(fields, kubernetesFields.includePodUid, prefix + kubernetesFields.podUidFieldName, info.podUid)
                mapFieldIfNotNull(fields, kubernetesFields.includeRestartCount, prefix + kubernetesFields.restartCountFieldName, info.restartCount.toString())
                mapFieldIfNotNull(fields, kubernetesFields.includeContainerName, prefix + kubernetesFields.containerNameFieldName, info.containerName)
                mapFieldIfNotNull(fields, kubernetesFields.includeContainerId, prefix + kubernetesFields.containerIdFieldName, info.containerId)
                mapFieldIfNotNull(fields, kubernetesFields.includeImageName, prefix + kubernetesFields.imageNameFieldName, info.imageName)
                mapFieldIfNotNull(fields, kubernetesFields.includeImageId, prefix + kubernetesFields.imageIdFieldName, info.imageId)
                mapFieldIfNotNull(fields, kubernetesFields.includeNodeIp, prefix + kubernetesFields.nodeIpFieldName, info.nodeIp)
                mapFieldIfNotNull(fields, kubernetesFields.includeNodeName, prefix + kubernetesFields.nodeNameFieldName, info.nodeName)

                if (kubernetesFields.includeLabels) {
                    val labelsPrefix = prefix + kubernetesFields.labelsPrefix
                    info.labels.forEach { (labelName, value) ->
                        mapField(fields, true, labelsPrefix + escapeDynamicLabelName(labelName), value)
                    }
                }

                if (kubernetesFields.includeAnnotations) {
                    val annotationsPrefix = prefix + kubernetesFields.annotationsPrefix
                    info.annotations.forEach { (annotationName, value) ->
                        mapField(fields, true, annotationsPrefix + escapeDynamicLabelName(annotationName), value)
                    }
                }
            }
        }
    }

    // loggerName is in most cases full qualified class name including packages, try to extract only name of class
    protected open fun extractLoggerClassName(loggerName: String): String {
        cachedLoggerClassNames[loggerName]?.let {
            return it
        }

        val indexOfDot = loggerName.lastIndexOf('.')

        val loggerClassName = if (indexOfDot >= 0) {
            loggerName.substring(indexOfDot + 1)
        } else {
            loggerName
        }

        cachedLoggerClassNames[loggerName] = loggerClassName

        return loggerClassName
    }

    open fun getStacktrace(exception: Throwable?): String? {
        if (config.includeStacktrace == false) {
            return null
        }

        return exception?.let {
            // Throwable doesn't implement hashCode(), so it differs for each new object -> create a hash code to uniquely identify Throwables
            val exceptionHash = getExceptionHashCode(exception)

            // exception.stackTraceToString() is one of the most resource intensive operations of our implementation. As there typically aren't
            // that much different exceptions in an application, we cache the stack trace of each unique exception for performance reasons.
            cachedStackTraces[exceptionHash]?.let {
                return it
            }

            var stackTrace = extractStacktrace(exception)
            if (escapeControlCharacters) {
                stackTrace = escapeControlCharacters(stackTrace)
            }

            cachedStackTraces[exceptionHash] = stackTrace

            stackTrace
        }
    }

    protected open fun extractStacktrace(exception: Throwable): String {
        val stackTrace = exception.stackTraceToString()

        return if (stackTrace.length > config.stacktraceMaxFieldLength) {
            stackTrace.substring(0, config.stacktraceMaxFieldLength)
        } else {
            stackTrace
        }
    }

    protected open fun getExceptionHashCode(exception: Throwable): Int {
        var hashCode = exception::class.hashCode()

        if (exception.message != null) {
            hashCode = 31 * hashCode + exception.message.hashCode()
        }

        // to avoid infinite loops check if exception.cause and exception equal
        if (exception.cause != null && exception.cause != exception) {
            hashCode = 31 * hashCode + getExceptionHashCode(exception.cause!!)
        }

        return hashCode
    }

    open fun escapeControlCharacters(value: String): String =
    // we have to escape single backslashes as Loki doesn't accept control characters
        // (returns then 400 Bad Request invalid control character found: 10, error found in #10 byte of ...)
        value.replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t")
            .replace("\"", "\\\"")

}