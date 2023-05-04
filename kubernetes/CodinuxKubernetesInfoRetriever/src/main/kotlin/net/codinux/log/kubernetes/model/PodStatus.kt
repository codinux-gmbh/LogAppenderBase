package net.codinux.log.kubernetes.model

import kotlinx.serialization.Serializable

@Serializable
data class PodStatus(
    val containerStatuses: List<ContainerStatus> = emptyList(),
    val hostIP: String? = null,
    val podIP: String? = null,
    val message: String? = null,
    val phase: PodPhase? = null,
    val qosClass: String? = null,
    val reason: String? = null,
    val startTime: Time? = null,
    val nominatedNodeName: String? = null,
    val ephemeralContainerStatuses: List<ContainerStatus> = emptyList(),
    val initContainerStatuses: List<ContainerStatus> = emptyList()
) {

    override fun toString(): String {
        return "$containerStatuses"
    }

}