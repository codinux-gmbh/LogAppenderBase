package net.codinux.log.data

expect class KubernetesInfoRetriever() {

    suspend fun retrieveKubernetesInfo(): KubernetesInfo?

}