package net.codinux.log.kubernetes

import net.codinux.log.statelogger.AppenderStateLogger

actual class KubernetesInfoRetrieverLocator actual constructor(stateLogger: AppenderStateLogger) {

    actual fun findKubernetesInfoRetrieverImplementations(): Set<KubernetesInfoRetriever> =
        emptySet()

}