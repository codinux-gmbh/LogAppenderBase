package net.codinux.log.kubernetes.model

import kotlinx.serialization.Serializable

@Serializable
data class Pod(
    val kind: String,
    val apiVersion: String,
    val metadata: ObjectMeta,
    val spec: PodSpec,
    val status: PodStatus
) {

    override fun toString(): String {
        return "$kind $metadata"
    }

}