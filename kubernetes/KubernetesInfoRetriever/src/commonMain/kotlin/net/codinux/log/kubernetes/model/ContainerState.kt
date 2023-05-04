package net.codinux.log.kubernetes.model

import kotlinx.serialization.Serializable

@Serializable
data class ContainerState(
    val running: ContainerStateRunning? = null,
    val terminated: ContainerStateTerminated? = null,
    val waiting: ContainerStateWaiting? = null
) {

    override fun toString(): String {
        return "running: $running, terminated = $terminated, waiting = $waiting"
    }

}