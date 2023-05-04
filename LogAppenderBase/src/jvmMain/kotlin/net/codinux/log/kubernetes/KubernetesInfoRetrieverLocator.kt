package net.codinux.log.kubernetes

import net.codinux.log.statelogger.AppenderStateLogger

actual open class KubernetesInfoRetrieverLocator actual constructor(
    protected open val stateLogger: AppenderStateLogger
) {

    actual open fun findKubernetesInfoRetrieverImplementations(): List<KubernetesInfoRetriever> {
        return mutableListOf<KubernetesInfoRetriever>().apply {
            findImplementation("net.codinux.log.kubernetes.Fabric8KubernetesInfoRetriever")?.let {
                add(it)
            }

            findImplementation("net.codinux.log.kubernetes.CodinuxKubernetesInfoRetriever")?.let {
                add(it)
            }
        }
    }

    protected open fun findImplementation(className: String): KubernetesInfoRetriever? {
        try {
            val kubernetesApiPodInfoRetrieverClass = Class.forName(className)

            try {
                val constructor = kubernetesApiPodInfoRetrieverClass.getDeclaredConstructor(AppenderStateLogger::class.java)

                val instance = constructor.newInstance(stateLogger)
                return instance as? KubernetesInfoRetriever
            } catch (e: Throwable) {
                stateLogger.error("Could not load KubernetesInfoRetriever", e)
            }
        } catch (ignored: Throwable) {
            // it's ok if class with className isn't found, it's dependency is not on the classpath then
        }

        return null
    }

}