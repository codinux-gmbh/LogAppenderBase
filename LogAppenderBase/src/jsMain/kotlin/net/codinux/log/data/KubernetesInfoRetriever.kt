package net.codinux.log.data

import net.codinux.log.statelogger.AppenderStateLogger

actual open class KubernetesInfoRetriever actual constructor(
    protected open val stateLogger: AppenderStateLogger
) {

    actual open suspend fun retrieveCurrentPodInfo(): PodInfo? =
        null

}