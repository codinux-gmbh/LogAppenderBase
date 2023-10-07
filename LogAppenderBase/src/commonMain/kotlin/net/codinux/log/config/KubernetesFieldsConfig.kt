package net.codinux.log.config

open class KubernetesFieldsConfig(
    open var includeNamespace: Boolean = IncludeNamespaceDefaultValue,
    open var namespaceFieldName: String = NamespaceDefaultFieldName,

    open var includePodName: Boolean = IncludePodNameDefaultValue,
    open var podNameFieldName: String = PodNameDefaultFieldName,

    open var includeContainerName: Boolean = IncludeContainerNameDefaultValue,
    open var containerNameFieldName: String = ContainerNameDefaultFieldName,

    open var includeImageName: Boolean = IncludeImageNameDefaultValue,
    open var imageNameFieldName: String = ImageNameDefaultFieldName,

    open var includeNodeName: Boolean = IncludeNodeNameDefaultValue,
    open var nodeNameFieldName: String = NodeNameDefaultFieldName,

    open var includeNodeIp: Boolean = IncludeNodeIpDefaultValue,
    open var nodeIpFieldName: String = NodeIpDefaultFieldName,

    open var includePodIp: Boolean = IncludePodIpDefaultValue,
    open var podIpFieldName: String = PodIpDefaultFieldName,

    open var includeStartTime: Boolean = IncludeStartTimeDefaultValue,
    open var startTimeFieldName: String = StartTimeDefaultFieldName,

    open var includeRestartCount: Boolean = IncludeRestartCountDefaultValue,
    open var restartCountFieldName: String = RestartCountDefaultFieldName,

    open var includePodUid: Boolean = IncludePodUidDefaultValue,
    open var podUidFieldName: String = PodUidDefaultFieldName,

    open var includeContainerId: Boolean = IncludeContainerIdDefaultValue,
    open var containerIdFieldName: String = ContainerIdDefaultFieldName,

    open var includeImageId: Boolean = IncludeImageIdDefaultValue,
    open var imageIdFieldName: String = ImageIdDefaultFieldName,

    open var includeLabels: Boolean = IncludeLabelsDefaultValue,
    open var labelsPrefix: String? = LabelsPrefixDefaultValue,

    open var includeAnnotations: Boolean = IncludeAnnotationsDefaultValue,
    open var annotationsPrefix: String? = AnnotationsPrefixDefaultValue
) {

    companion object {

        private const val True = true
        private const val False = false


        const val IncludeNamespaceDefaultValue = True
        const val IncludeNamespaceDefaultValueString = IncludeNamespaceDefaultValue.toString()
        const val NamespaceDefaultFieldName = "namespace"

        const val IncludePodNameDefaultValue = True
        const val IncludePodNameDefaultValueString = IncludePodNameDefaultValue.toString()
        const val PodNameDefaultFieldName = "podName"

        const val IncludeContainerNameDefaultValue = True
        const val IncludeContainerNameDefaultValueString = IncludeContainerNameDefaultValue.toString()
        const val ContainerNameDefaultFieldName = "containerName"

        const val IncludeImageNameDefaultValue = False
        const val IncludeImageNameDefaultValueString = IncludeImageNameDefaultValue.toString()
        const val ImageNameDefaultFieldName = "imageName"

        const val IncludeNodeNameDefaultValue = False
        const val IncludeNodeNameDefaultValueString = IncludeNodeNameDefaultValue.toString()
        const val NodeNameDefaultFieldName = "node"

        const val IncludeNodeIpDefaultValue = False
        const val IncludeNodeIpDefaultValueString = IncludeNodeIpDefaultValue.toString()
        const val NodeIpDefaultFieldName = "nodeIp"

        const val IncludePodIpDefaultValue = False
        const val IncludePodIpDefaultValueString = IncludePodIpDefaultValue.toString()
        const val PodIpDefaultFieldName = "podIp"

        const val IncludeStartTimeDefaultValue = False
        const val IncludeStartTimeDefaultValueString = IncludeStartTimeDefaultValue.toString()
        const val StartTimeDefaultFieldName = "startTime"

        const val IncludeRestartCountDefaultValue = False
        const val IncludeRestartCountDefaultValueString = IncludeRestartCountDefaultValue.toString()
        const val RestartCountDefaultFieldName = "restartCount"

        const val IncludePodUidDefaultValue = False
        const val IncludePodUidDefaultValueString = IncludePodUidDefaultValue.toString()
        const val PodUidDefaultFieldName = "podUid"

        const val IncludeContainerIdDefaultValue = False
        const val IncludeContainerIdDefaultValueString = IncludeContainerIdDefaultValue.toString()
        const val ContainerIdDefaultFieldName = "containerId"

        const val IncludeImageIdDefaultValue = False
        const val IncludeImageIdDefaultValueString = IncludeImageIdDefaultValue.toString()
        const val ImageIdDefaultFieldName = "imageId"

        const val IncludeLabelsDefaultValue = False
        const val IncludeLabelsDefaultValueString = IncludeLabelsDefaultValue.toString()
        const val LabelsPrefixDefaultValue: String = "label"

        const val IncludeAnnotationsDefaultValue = False
        const val IncludeAnnotationsDefaultValueString = IncludeAnnotationsDefaultValue.toString()
        const val AnnotationsPrefixDefaultValue: String = "annotation"
    }
}