package net.codinux.log.mapper

import net.codinux.kotlin.concurrent.collections.ConcurrentMap
import net.codinux.log.LogRecord
import net.codinux.log.config.KubernetesFieldsConfig
import net.codinux.log.kubernetes.PodInfo
import net.codinux.log.stacktrace.StackTraceFormatter
import net.codinux.log.stacktrace.StackTraceFormatterOptions

open class FieldMapper(
    // Loki does not allow null values in Stream / fields
    open val allowNullAsFieldValue: Boolean = true,
    open val escapeControlCharacters: Boolean = true,
    open val fieldEscaper: FieldEscaper? = null,
    open val stackTraceFormatter: StackTraceFormatter = StackTraceFormatter.Default
) {

    protected open val cachedStackTraces = ConcurrentMap<Int?, String>()

    protected open val cachedLoggerClassNames = ConcurrentMap<String, String>()


    open fun mapField(fields: MutableMap<String, String?>, includeField: Boolean, fieldName: String, valueSupplier: () -> String?) {
        if (includeField) {
            mapField(fields, includeField, fieldName, valueSupplier.invoke())
        }
    }

    open fun mapField(fields: MutableMap<String, String?>, includeField: Boolean, fieldName: String, value: String?) {
        if (includeField) {
            // Loki returns 400 Bad request if a stream value is null. Assure that only fields with value != null get added to Stream
            if (value != null || allowNullAsFieldValue) {
                fields[fieldName] = value
            } else {
                fields.remove(fieldName)
            }
        }
    }

    open fun mapFieldIfNotNull(fields: MutableMap<String, String?>, includeField: Boolean, fieldName: String, value: String?) {
        mapField(fields, includeField && value != null, fieldName, value)
    }

    open fun mapDynamicFieldIfNotNull(fields: MutableMap<String, String?>, includeField: Boolean, fieldName: String, value: String?) {
        if (includeField) {
            if (value.isNullOrEmpty()) {
                fields.remove(fieldName)
            } else {
                fields[fieldName] = value
            }
        }
    }

    open fun <T> mapMdcFields(record: LogRecord<T>, fields: MutableMap<String, String?>, includeMdc: Boolean, mdc: Map<String, String>?, mdcKeysPrefix: String?) {
        if (includeMdc) {
            record.dynamicFields.forEach { name ->
                fields.remove(name)
            }

            if (mdc != null) {
                mdc.mapNotNull { (key, value) ->
                    val fieldName = mdcKeysPrefix + escapeDynamicLabelName(key)
                    record.dynamicFields.add(fieldName)
                    mapField(fields, true, fieldName, value)
                }
            }
        }
    }

    protected open fun escapeDynamicLabelName(key: String): String =
        fieldEscaper?.escapeFieldName(key) ?: key

    open fun mapPodInfoFields(fields: MutableMap<String, String?>, includeKubernetesInfo: Boolean, podInfo: PodInfo?, kubernetesFieldsPrefix: String?, kubernetesFields: KubernetesFieldsConfig) {
        if (includeKubernetesInfo) {
            podInfo?.let { info ->
                val prefix = kubernetesFieldsPrefix
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

    /**
     * loggerName is in most cases full qualified class name including packages, try to extract only name of class
     */
    open fun extractLoggerClassName(loggerName: String): String {
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

    open fun getStacktrace(exception: Throwable?, includeStacktrace: Boolean, stacktraceMaxFieldLength: Int? = null): String? {
        if (includeStacktrace == false) {
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

            var stackTrace = extractStacktrace(exception, stacktraceMaxFieldLength)
            if (escapeControlCharacters) {
                stackTrace = escapeControlCharacters(stackTrace)
            }

            cachedStackTraces[exceptionHash] = stackTrace

            stackTrace
        }
    }

    protected open fun extractStacktrace(exception: Throwable, stacktraceMaxFieldLength: Int?): String =
        stackTraceFormatter.format(exception, StackTraceFormatterOptions(maxStackTraceStringLength = stacktraceMaxFieldLength))

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