package net.codinux.log.kubernetes.model

import kotlinx.serialization.Serializable

@Serializable
data class ObjectMeta(
    val name: String = "",
    val namespace: String = "",
    val uid: String? = null,
    val creationTimestamp: Time? = null,
    val labels: Map<String, String> = emptyMap(),
    val annotations: Map<String, String> = emptyMap()
) {

    override fun toString(): String {
        return "$namespace/$name"
    }

}