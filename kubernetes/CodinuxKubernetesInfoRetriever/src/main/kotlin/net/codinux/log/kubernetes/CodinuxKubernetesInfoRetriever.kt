package net.codinux.log.kubernetes

import net.codinux.log.kubernetes.model.Pod
import net.codinux.log.kubernetes.web.JavaWebClient
import net.codinux.log.kubernetes.web.WebClient
import net.codinux.log.statelogger.AppenderStateLogger
import net.codinux.log.statelogger.StdOutStateLogger
import java.io.File
import java.net.InetAddress

class CodinuxKubernetesInfoRetriever(
    private val stateLogger: AppenderStateLogger = StdOutStateLogger()
) : KubernetesInfoRetriever {

    companion object {
        private val KubernetesSecretsFolder = File("/run/secrets/kubernetes.io/serviceaccount/")
    }


    override suspend fun retrieveCurrentPodInfo(): PodInfo? {
        try {
            val namespaceFile = File(KubernetesSecretsFolder, "namespace")
            if (namespaceFile.exists() == false) {
                stateLogger.info("Not running in a Kubernetes environment, file '/run/secrets/kubernetes.io/serviceaccount/namespace' does not exist.")
                return null
            }

            val namespace = namespaceFile.readText()
            val accessToken = File(KubernetesSecretsFolder, "token").readText()
            val kubeApiCertificate: String? = try {
                File(KubernetesSecretsFolder, "ca.crt").readText().ifBlank { null }
            } catch (ignored: Throwable) { // there are situations where the ca.crt file does not exist
                null
            }

            val localHost = InetAddress.getLocalHost()
            val podName = localHost.hostName
            val podIp = localHost.hostAddress

            return retrievePodInfo(podName, podIp, namespace, accessToken, kubeApiCertificate)
        } catch (e: Throwable) {
            stateLogger.error("Could not retrieve any pod info, no cluster or container info will be added to logs", e)
        }

        return null
    }

    suspend fun retrievePodInfo(podName: String, podIp: String, namespace: String, accessToken: String, kubeApiCertificate: String?): PodInfo? {
        try {
            val host = System.getenv("KUBERNETES_SERVICE_HOST")
            val port = System.getenv("KUBERNETES_SERVICE_PORT_HTTPS")

            val apiServer = if (host != null && port != null) "https://$host:$port"
                            else "https://kubernetes.default.svc:443"
            val url = "$apiServer/api/v1/namespaces/$namespace/pods/$podName"

            val client: WebClient = JavaWebClient(accessToken, kubeApiCertificate, stateLogger)

            val response = client.get(url)

            if (response.isSuccess && response.responseBody != null) {
                return mapPodResponse(response.responseBody, namespace, podName, podIp)
            } else {
                if (response.statusCode == 403) {
                    stateLogger.error("Access to Pod information is forbidden. Did you add the privilege to read pod information to your pod's ServiceAccount? (see https://github.com/codinux-gmbh/ElasticsearchLogger#kubernetes-info)")
                } else {
                    stateLogger.error("Could not retrieve Pod information: ${response.statusCode}", response.error)
                }
            }
        } catch (e: Throwable) {
            stateLogger.error("Could not retrieve Pod information", e)
        }

        return PodInfo(namespace, podName, podIp)
    }


    private fun mapPodResponse(pod: Pod?, namespace: String, podName: String, podIp: String) = pod?.let {
        // TODO: find the container we are running in. Till then we simply take the first container
        val containerStatus = pod.status.containerStatuses.firstOrNull()
        val container = pod.spec.containers.firstOrNull()
        val startTime = containerStatus?.state?.running?.startedAt
            ?: containerStatus?.state?.terminated?.startedAt
            ?: pod.status.startTime // but be aware `startTime` returns Pod-, not Container start time

        PodInfo(
            pod.metadata.namespace.ifBlank { namespace },
            pod.metadata.name.ifBlank { podName },
            pod.status.podIP ?: podIp,
            pod.metadata.uid,
            startTime,
            containerStatus?.restartCount,
            container?.name ?: containerStatus?.name,
            containerStatus?.containerID,
            container?.image ?: containerStatus?.image,
            containerStatus?.imageID,
            pod.status.hostIP,
            pod.spec.nodeName,
            pod.metadata.labels,
            pod.metadata.annotations
        )
    }
}