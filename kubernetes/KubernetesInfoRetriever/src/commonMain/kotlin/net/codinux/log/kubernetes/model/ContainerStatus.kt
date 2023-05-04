package net.codinux.log.kubernetes.model

import kotlinx.serialization.Serializable

@Serializable
data class ContainerStatus(
    val name: String,
    val image: String,
    val imageID: String,
    val restartCount: Int,
    val ready: Boolean,
    val started: Boolean = false,
    val containerID: String? = null,
    val state: ContainerState? = null,
    val lastState: ContainerState? = null
) {

    override fun toString(): String {
        return "$name ($image) ready? $ready"
    }

}