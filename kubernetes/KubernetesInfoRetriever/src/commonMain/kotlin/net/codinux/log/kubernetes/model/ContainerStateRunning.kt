package net.codinux.log.kubernetes.model

import kotlinx.serialization.Serializable

@Serializable
data class ContainerStateRunning(
    val startedAt: Time? = null
) {

    override fun toString(): String {
        return "$startedAt"
    }

}