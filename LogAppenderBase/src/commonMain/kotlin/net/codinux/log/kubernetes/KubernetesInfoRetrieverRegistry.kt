package net.codinux.log.kubernetes

import net.codinux.log.statelogger.AppenderStateLogger
import net.codinux.log.statelogger.StdOutStateLogger

open class KubernetesInfoRetrieverRegistry(
    protected open val stateLogger: AppenderStateLogger
) : KubernetesInfoRetriever {

    companion object {

        var Registry = KubernetesInfoRetrieverRegistry(StdOutStateLogger())

        fun init(stateLogger: AppenderStateLogger) {
            Registry = KubernetesInfoRetrieverRegistry(stateLogger)
        }

    }


    private val implementations = mutableSetOf<KubernetesInfoRetriever>()

    init {
        KubernetesInfoRetrieverLocator(stateLogger).findKubernetesInfoRetrieverImplementations().forEach {
            register(it)
        }
    }

    open fun register(infoRetriever: KubernetesInfoRetriever) {
        implementations.add(infoRetriever)
    }

    override suspend fun retrieveCurrentPodInfo(): PodInfo? {
        if (implementations.isEmpty()) {
            stateLogger.error("No KubernetesInfoRetriever implementation found. Did you add a KubernetesInfoRetriever implementation, e.g. " +
                    "net.codinux.log.kubernetes:codinux-kubernetes-info-retriever or net.codinux.log.kubernetes:io.fabric8:kubernetes-client?")
            return null
        }

        if (implementations.size > 1) {
            stateLogger.warn("Multiple KubernetesInfoRetriever implementations found: ${implementations.map { it::class.simpleName }.joinToString()}. Using first one.")
        }

        return implementations.firstOrNull()?.retrieveCurrentPodInfo()
    }
}