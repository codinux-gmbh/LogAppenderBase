package net.codinux.log.kubernetes.model

enum class PodPhase {
    Failed,
    Pending,
    Running,
    Succeeded,
    Unknown
}