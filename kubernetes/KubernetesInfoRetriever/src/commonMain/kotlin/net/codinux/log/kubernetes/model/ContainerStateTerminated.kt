package net.codinux.log.kubernetes.model

import kotlinx.serialization.Serializable

@Serializable
data class ContainerStateTerminated(
    val exitCode: Int,
    val containerID: String? = null,
    val finishedAt: Time? = null,
    val message: String? = null,
    val reason: String? = null,
    val signal: Int? = null,
    val startedAt: Time? = null
) {

    override fun toString(): String {
        return "$exitCode $signal $reason $message"
    }

}