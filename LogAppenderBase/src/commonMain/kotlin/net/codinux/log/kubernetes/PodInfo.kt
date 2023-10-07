package net.codinux.log.kubernetes

open class PodInfo(
    open val namespace: String,
    /**
     * The pod name like "grafana-56f4cd858f-wdrpl". Equals generally [net.codinux.log.data.ProcessData.hostName].
     */
    open val podName: String,
    /**
     * Equals generally [net.codinux.log.data.ProcessData.hostIp].
     */
    open val podIp: String,
    open val podUid: String? = null,
    /**
     * The container start time.
     */
    open val startTime: String? = null,
    /**
     * The container restart count.
     */
    open val restartCount: Int? = null,
    open val containerName: String? = null,
    /**
     * A string containing the container id like "containerd://fbd1fb3655c76f71c36b46ea00b646eb59f93726e15be5a97fabebaaada918ab".
     */
    open val containerId: String? = null,
    /**
     * Contains the version / image tag like "docker.io/grafana/loki:2.8.0".
     */
    open val imageName: String? = null,
    /**
     * A longer string containing the container SHA like "docker.io/grafana/loki@sha256:0d28e4dddd4ccce1772544a501e8b8f96595accbb74ee8cd0415bce81dd2edfc".
     */
    open val imageId: String? = null,
    open val nodeIp: String? = null,
    open val nodeName: String? = null,
    open val labels: Map<String, String> = mapOf(),
    open val annotations: Map<String, String> = mapOf()
) {

    override fun toString(): String {
        return "$namespace/$podName $startTime"
    }

}