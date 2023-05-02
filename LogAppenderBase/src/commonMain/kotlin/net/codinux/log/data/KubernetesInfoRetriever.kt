package net.codinux.log.data

import net.codinux.log.statelogger.AppenderStateLogger

expect class KubernetesInfoRetriever(stateLogger: AppenderStateLogger) {

    suspend fun retrieveKubernetesInfo(): KubernetesInfo?

}