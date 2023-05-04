package net.codinux.log.kubernetes.model

import kotlinx.serialization.Serializable

@Serializable
data class PodSpec(
    val containers: List<Container> = emptyList(),
    val nodeName: String? = null,
    val serviceAccount: String? = null,
    val serviceAccountName: String? = null
) {

    override fun toString(): String {
        return "$containers"
    }

}