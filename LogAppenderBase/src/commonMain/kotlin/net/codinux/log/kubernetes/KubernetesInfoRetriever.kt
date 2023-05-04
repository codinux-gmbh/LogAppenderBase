package net.codinux.log.kubernetes

interface KubernetesInfoRetriever {

    suspend fun retrieveCurrentPodInfo(): PodInfo?

}