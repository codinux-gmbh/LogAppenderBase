package net.codinux.log.mapper

import net.codinux.log.LogRecord
import net.codinux.log.config.LogAppenderFieldsConfig
import net.codinux.log.data.ProcessData
import net.codinux.log.kubernetes.PodInfo

open class LogRecordMapper(
    protected open val config: LogAppenderFieldsConfig,
    protected open var fieldMapper: FieldMapper = FieldMapper(),
) {

    open lateinit var processData: ProcessData

    open var podInfo: PodInfo? = null


    /**
     * Add fields that never change during the whole process lifetime
     */
    open fun mapStaticFields(fields: MutableMap<String, String?>) {
        mapField(fields, config.includeAppName, config.appNameFieldName, config.appName)
        mapField(fields, config.includeAppVersion, config.appVersionFieldName, config.appVersion)
        mapField(fields, config.includeJobName, config.jobNameFieldName, config.jobName)
        mapField(fields, config.includeHostName, config.hostNameFieldName, processData.hostName)
        mapField(fields, config.includeHostIp, config.hostIpFieldName, processData.hostIp)

        fieldMapper.mapPodInfoFields(fields, config.includeKubernetesInfo, podInfo, config.kubernetesFieldsPrefix, config.kubernetesFields)
    }

    open fun <T> mapLogEventFields(record: LogRecord<T>, fields: MutableMap<String, String?>) {
        mapField(fields, config.includeLogLevel, config.logLevelFieldName, record.level)
        mapField(fields, config.includeLoggerName, config.loggerNameFieldName, record.loggerName)
        mapField(fields, config.includeLoggerClassName, config.loggerClassNameFieldName) { record.loggerName?.let { fieldMapper.extractLoggerClassName(it) } }
        mapField(fields, config.includeThreadName, config.threadNameFieldName, record.threadName)

        mapDynamicFieldIfNotNull(fields, config.includeStacktrace, config.stacktraceFieldName, getStacktrace(record.exception))

        fieldMapper.mapMdcFields(record, fields, config.includeMdc && record.mdc != null, record.mdc, config.mdcKeysPrefix)
        mapDynamicFieldIfNotNull(fields, config.includeMarker, config.markerFieldName, record.marker)
        mapDynamicFieldIfNotNull(fields, config.includeNdc, config.ndcFieldName, record.ndc)
    }


    protected open fun mapField(fields: MutableMap<String, String?>, includeField: Boolean, fieldName: String, valueSupplier: () -> String?) =
        fieldMapper.mapField(fields, includeField, fieldName, valueSupplier)

    protected open fun mapField(fields: MutableMap<String, String?>, includeField: Boolean, fieldName: String, value: String?) =
        fieldMapper.mapField(fields, includeField, fieldName, value)

    protected open fun mapDynamicFieldIfNotNull(fields: MutableMap<String, String?>, includeField: Boolean, fieldName: String, value: String?) =
        fieldMapper.mapDynamicFieldIfNotNull(fields, includeField, fieldName, value)


    open fun getStacktrace(exception: Throwable?): String? =
        fieldMapper.getStacktrace(exception, config.includeStacktrace, config.stacktraceMaxFieldLength)

}