package net.codinux.log.kubernetes.model

import kotlinx.serialization.Serializable

@Serializable
data class ContainerStateWaiting(
    val message: String? = null,
    val reason: String? = null
) {

    override fun toString(): String {
        return "$reason $message"
    }

}