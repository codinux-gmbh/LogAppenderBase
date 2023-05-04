package net.codinux.log.kubernetes

import net.codinux.log.statelogger.AppenderStateLogger

actual open class KubernetesInfoRetrieverLocator actual constructor(
    protected open val stateLogger: AppenderStateLogger
) {

    actual open fun findKubernetesInfoRetrieverImplementations(): Set<KubernetesInfoRetriever> {
        return mutableSetOf<KubernetesInfoRetriever>().apply {
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
            val kubernetesInfoRetrieverClass = Class.forName(className)

            try {
                val constructors = kubernetesInfoRetrieverClass.declaredConstructors

                // first try to find constructor that takes an AppenderStateLogger as single argument
                val instance = constructors.firstOrNull { it.parameterTypes.size == 1 && it.parameterTypes.contains(AppenderStateLogger::class.java) }
                    ?.newInstance(stateLogger)

                    // if that one cannot be found try to find parameterless constructor
                    ?: constructors.firstOrNull { it.parameterTypes.isEmpty() }
                        ?.newInstance()

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