package net.codinux.log.kubernetes

import net.codinux.log.statelogger.AppenderStateLogger

expect class KubernetesInfoRetriever(stateLogger: AppenderStateLogger) {

    suspend fun retrieveCurrentPodInfo(): PodInfo?

}