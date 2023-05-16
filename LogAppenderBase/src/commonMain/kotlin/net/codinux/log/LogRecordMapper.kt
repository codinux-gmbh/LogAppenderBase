package net.codinux.log

import net.codinux.log.data.ProcessData
import net.codinux.log.kubernetes.PodInfo

open class LogRecordMapper(
    protected open val config: LogAppenderConfig,
    protected open val processData: ProcessData,
    open var podInfo: PodInfo? = null,
    open var escapeControlCharacters: Boolean = true
) {

    protected open val cachedStackTraces = mutableMapOf<Int?, String>() // TODO: use thread safe Map

    protected open val cachedLoggerClassNames = mutableMapOf<String, String>() // TODO: use thread safe Map

    /**
     * Add fields that never change during the whole process lifetime
     */
    open fun mapStaticFields(fields: MutableMap<String, String?>) {
        mapField(fields, config.includeHostName, config.hostNameFieldName, processData.hostName)
        mapField(fields, config.includeAppName, config.appNameFieldName, config.appName)

        mapPodInfoFields(fields)

        // pre-allocate per log event fields in Map
        mapLogEventFields(fields, "", "", "", null, null, null, null)
    }

    open fun mapLogEventFields(
        fields: MutableMap<String, String?>,
        level: String,
        loggerName: String?,
        threadName: String?,
        exception: Throwable?,
        mdc: Map<String, String>?,
        marker: String?,
        ndc: String?
    ) {
        removeDynamicFields(fields)

        mapField(fields, config.includeLogLevel, config.logLevelFieldName, level)
        mapField(fields, config.includeLoggerName, config.loggerNameFieldName, loggerName)
        mapField(fields, config.includeLoggerClassName, config.loggerClassNameFieldName) { loggerName?.let { extractLoggerClassName(it) } }

        mapMdcFields(fields, config.includeMdc && mdc != null, mdc)
        mapField(fields, config.includeMarker, config.markerFieldName, marker)
        mapField(fields, config.includeNdc, config.ndcFieldName, ndc)
    }

    protected open fun removeDynamicFields(fields: MutableMap<String, String?>) {
        if (config.includeMdc && config.mdcKeysPrefix != null) {
            fields.keys.filter { it.startsWith(config.mdcKeysPrefix!!) }.forEach { mdcField ->
                fields.remove(mdcField)
            }
        }

        if (config.includeMarker) {
            fields.remove(config.markerFieldName)
        }

        if (config.includeNdc) {
            fields.remove(config.ndcFieldName)
        }
    }

    protected open fun mapField(fields: MutableMap<String, String?>, includeField: Boolean, fieldName: String, valueSupplier: () -> String?) {
        if (includeField) {
            mapField(fields, includeField, fieldName, valueSupplier.invoke())
        }
    }

    protected open fun mapField(fields: MutableMap<String, String?>, includeField: Boolean, fieldName: String, value: String?) {
        if (includeField) {
            fields[fieldName] = value
        }
    }

    protected open fun mapFieldIfNotNull(fields: MutableMap<String, String?>, fieldName: String, value: String?) {
        mapField(fields, value != null, fieldName, value)
    }

    protected open fun mapMdcFields(fields: MutableMap<String, String?>, includeMdc: Boolean, mdc: Map<String, String>?) {
        if (includeMdc) {
            if (mdc != null) {
                val prefix = config.mdcKeysPrefix

                mdc.mapNotNull { (key, value) ->
                    mapField(fields, true, prefix + key, value)
                }
            }
        }
    }

    protected open fun mapPodInfoFields(fields: MutableMap<String, String?>) {
        if (config.includeKubernetesInfo) {
            podInfo?.let { info ->
                val prefix = config.kubernetesFieldsPrefix

                mapField(fields, true, prefix + "namespace", info.namespace)
                mapField(fields, true, prefix + "podName", info.podName)
                mapField(fields, true, prefix + "podIp", info.podIp)
                mapField(fields, true, prefix + "startTime", info.startTime)

                mapFieldIfNotNull(fields, prefix + "podUid", info.podUid)
                mapFieldIfNotNull(fields, prefix + "restartCount", info.restartCount.toString())
                mapFieldIfNotNull(fields, prefix + "containerName", info.containerName)
                mapFieldIfNotNull(fields, prefix + "containerId", info.containerId)
                mapFieldIfNotNull(fields, prefix + "imageName", info.imageName)
                mapFieldIfNotNull(fields, prefix + "imageId", info.imageId)
                mapFieldIfNotNull(fields, prefix + "nodeIp", info.nodeIp)
                mapFieldIfNotNull(fields, prefix + "node", info.nodeName)
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