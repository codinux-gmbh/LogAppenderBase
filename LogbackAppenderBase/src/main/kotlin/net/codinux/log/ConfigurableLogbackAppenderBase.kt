package net.codinux.log

import net.codinux.log.config.LogAppenderConfig


/**
 * As Logback sets configuration directly on appender class and LogAppenderConfig has a lot of settings, this class
 * wraps all settings and applies them to [config] property.
 */
abstract class ConfigurableLogbackAppenderBase(
    protected open val config: LogAppenderConfig
) : LogbackAppenderBase() {

  protected open val fields = config.fields

  protected open val kubernetesFields = fields.kubernetesFields


  open fun setEnabled(enabled: Boolean) {
    config.enabled = enabled
  }

  open fun setStateLoggerName(stateLoggerName: String?) {
    config.stateLoggerName = stateLoggerName
  }


  /*    Writer Config      */

  open fun setHostUrl(hostUrl: String) {
    config.writer.hostUrl = hostUrl
  }

  open fun setUsername(username: String?) {
    config.writer.username = username
  }

  open fun setPassword(password: String?) {
    config.writer.password = password
  }

  open fun setMaxBufferedLogRecords(maxBufferedLogRecords: Int) {
    config.writer.maxBufferedLogRecords = maxBufferedLogRecords
  }

  open fun setMaxLogRecordsPerBatch(maxLogRecordsPerBatch: Int) {
    config.writer.maxLogRecordsPerBatch = maxLogRecordsPerBatch
  }

  open fun setSendLogRecordsPeriodMillis(sendLogRecordsPeriodMillis: Long) {
    config.writer.sendLogRecordsPeriodMillis = sendLogRecordsPeriodMillis
  }

  open fun setConnectTimeoutMillis(connectTimeoutMillis: Long) {
    config.writer.connectTimeoutMillis = connectTimeoutMillis
  }

  open fun setRequestTimeoutMillis(requestTimeoutMillis: Long) {
    config.writer.requestTimeoutMillis = requestTimeoutMillis
  }


  /*    Fields Config      */

  open fun setIncludeLogLevel(includeLogLevel: Boolean) {
    fields.includeLogLevel = includeLogLevel
  }

  open fun setLogLevelFieldName(logLevelFieldName: String) {
    fields.logLevelFieldName = logLevelFieldName
  }

  open fun setIncludeLoggerName(includeLogger: Boolean) {
    fields.includeLoggerName = includeLogger
  }

  open fun setLoggerNameFieldName(loggerFieldName: String) {
    fields.loggerNameFieldName = loggerFieldName
  }

  open fun setIncludeLoggerClassName(includeLoggerName: Boolean) {
    fields.includeLoggerClassName = includeLoggerName
  }

  open fun setLoggerClassNameFieldName(loggerNameFieldName: String) {
    fields.loggerClassNameFieldName = loggerNameFieldName
  }

  open fun setIncludeThreadName(includeThreadName: Boolean) {
    fields.includeThreadName = includeThreadName
  }

  open fun setThreadNameFieldName(threadNameFieldName: String) {
    fields.threadNameFieldName = threadNameFieldName
  }

  open fun setIncludeAppName(includeAppName: Boolean) {
    fields.includeAppName = includeAppName
  }

  open fun setAppNameFieldName(appNameFieldName: String) {
    fields.appNameFieldName = appNameFieldName
  }

  open fun setAppName(appName: String) {
    fields.appName = appName
  }

  open fun setIncludeAppVersion(includeAppVersion: Boolean) {
    fields.includeAppVersion = includeAppVersion
  }

  open fun setAppVersionFieldName(appVersionFieldName: String) {
    fields.appVersionFieldName = appVersionFieldName
  }

  open fun setAppVersion(appVersion: String) {
    fields.appVersion = appVersion
  }

  open fun setIncludeJobName(includeJobName: Boolean) {
    fields.includeJobName = includeJobName
  }

  open fun setJobNameFieldName(jobNameFieldName: String) {
    fields.jobNameFieldName = jobNameFieldName
  }

  open fun setJobName(jobName: String) {
    fields.jobName = jobName
  }

  open fun setIncludeHostName(includeHostName: Boolean) {
    fields.includeHostName = includeHostName
  }

  open fun setHostNameFieldName(hostNameFieldName: String) {
    fields.hostNameFieldName = hostNameFieldName
  }

  open fun setIncludeHostIp(includeHostIp: Boolean) {
    fields.includeHostIp = includeHostIp
  }

  open fun setHostIpFieldName(hostIpFieldName: String) {
    fields.hostIpFieldName = hostIpFieldName
  }

  open fun setIncludeStacktrace(includeStacktrace: Boolean) {
    fields.includeStacktrace = includeStacktrace
  }

  open fun setStacktraceFieldName(stacktraceFieldName: String) {
    fields.stacktraceFieldName = stacktraceFieldName
  }

  open fun setStacktraceMaxFieldLength(stacktraceMaxFieldLength: Int) {
    fields.stacktraceMaxFieldLength = stacktraceMaxFieldLength
  }

  open fun setIncludeMdc(includeMdc: Boolean) {
    fields.includeMdc = includeMdc
  }

  open fun setMdcKeysPrefix(mdcKeysPrefix: String) {
    fields.mdcKeysPrefix = mdcKeysPrefix
  }

  open fun setIncludeMarker(includeMarker: Boolean) {
    fields.includeMarker = includeMarker
  }

  open fun setMarkerFieldName(markerFieldName: String) {
    fields.markerFieldName = markerFieldName
  }

  open fun setIncludeNdc(includeNdc: Boolean) {
    fields.includeNdc = includeNdc
  }

  open fun setNdcFieldName(ndcFieldName: String) {
    fields.ndcFieldName = ndcFieldName
  }


  /*    Kubernetes Fields Config      */

  open fun setIncludeKubernetesInfo(includeKubernetesInfo: Boolean) {
    fields.includeKubernetesInfo = includeKubernetesInfo
  }

  open fun setKubernetesFieldsPrefix(kubernetesFieldsPrefix: String) {
    fields.kubernetesFieldsPrefix = kubernetesFieldsPrefix
  }

  open fun setIncludeNamespace(includeNamespace: Boolean) {
    kubernetesFields.includeNamespace = includeNamespace
  }

  open fun setNamespaceFieldName(namespaceFieldName: String) {
    kubernetesFields.namespaceFieldName = namespaceFieldName
  }

  open fun setIncludePodName(includePodName: Boolean) {
    kubernetesFields.includePodName = includePodName
  }

  open fun setPodNameFieldName(podNameFieldName: String) {
    kubernetesFields.podNameFieldName = podNameFieldName
  }

  open fun setIncludeContainerName(includeContainerName: Boolean) {
    kubernetesFields.includeContainerName = includeContainerName
  }

  open fun setContainerNameFieldName(containerNameFieldName: String) {
    kubernetesFields.containerNameFieldName = containerNameFieldName
  }

  open fun setIncludeImageName(includeImageName: Boolean) {
    kubernetesFields.includeImageName = includeImageName
  }

  open fun setImageNameFieldName(imageNameFieldName: String) {
    kubernetesFields.imageNameFieldName = imageNameFieldName
  }

  open fun setIncludeNodeName(includeNodeName: Boolean) {
    kubernetesFields.includeNodeName = includeNodeName
  }

  open fun setNodeNameFieldName(nodeNameFieldName: String) {
    kubernetesFields.nodeNameFieldName = nodeNameFieldName
  }

  open fun setIncludeNodeIp(includeNodeIp: Boolean) {
    kubernetesFields.includeNodeIp = includeNodeIp
  }

  open fun setNodeIpFieldName(nodeIpFieldName: String) {
    kubernetesFields.nodeIpFieldName = nodeIpFieldName
  }

  open fun setIncludePodIp(includePodIp: Boolean) {
    kubernetesFields.includePodIp = includePodIp
  }

  open fun setPodIpFieldName(podIpFieldName: String) {
    kubernetesFields.podIpFieldName = podIpFieldName
  }

  open fun setIncludeStartTime(includeStartTime: Boolean) {
    kubernetesFields.includeStartTime = includeStartTime
  }

  open fun setStartTimeFieldName(startTimeFieldName: String) {
    kubernetesFields.startTimeFieldName = startTimeFieldName
  }

  open fun setIncludeRestartCount(includeRestartCount: Boolean) {
    kubernetesFields.includeRestartCount = includeRestartCount
  }

  open fun setRestartCountFieldName(restartCountFieldName: String) {
    kubernetesFields.restartCountFieldName = restartCountFieldName
  }

  open fun setIncludePodUid(includePodUid: Boolean) {
    kubernetesFields.includePodUid = includePodUid
  }

  open fun setPodUidFieldName(podUidFieldName: String) {
    kubernetesFields.podUidFieldName = podUidFieldName
  }

  open fun setIncludeContainerId(includeContainerId: Boolean) {
    kubernetesFields.includeContainerId = includeContainerId
  }

  open fun setContainerIdFieldName(containerIdFieldName: String) {
    kubernetesFields.containerIdFieldName = containerIdFieldName
  }

  open fun setIncludeImageId(includeImageId: Boolean) {
    kubernetesFields.includeImageId = includeImageId
  }

  open fun setImageIdFieldName(imageIdFieldName: String) {
    kubernetesFields.imageIdFieldName = imageIdFieldName
  }

  open fun setIncludeLabels(includeLabels: Boolean) {
    kubernetesFields.includeLabels = includeLabels
  }

  open fun setLabelsPrefix(labelsPrefix: String) {
    kubernetesFields.labelsPrefix = labelsPrefix
  }

  open fun setIncludeAnnotations(includeAnnotations: Boolean) {
    kubernetesFields.includeAnnotations = includeAnnotations
  }

  open fun setAnnotationsPrefix(annotationsPrefix: String) {
    kubernetesFields.annotationsPrefix = annotationsPrefix
  }

}