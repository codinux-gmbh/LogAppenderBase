package net.codinux.log.kubernetes

import net.codinux.log.statelogger.AppenderStateLogger

expect class KubernetesInfoRetrieverLocator(stateLogger: AppenderStateLogger) {

    fun findKubernetesInfoRetrieverImplementations(): Set<KubernetesInfoRetriever>

}