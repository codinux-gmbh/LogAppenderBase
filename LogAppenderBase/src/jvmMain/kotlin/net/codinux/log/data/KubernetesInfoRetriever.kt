package net.codinux.log.data

import io.fabric8.kubernetes.api.model.Pod
import io.fabric8.kubernetes.client.KubernetesClient
import io.fabric8.kubernetes.client.KubernetesClientBuilder
import io.fabric8.kubernetes.client.okhttp.OkHttpClientImpl
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.LoggerFactory
import java.io.File
import java.net.InetAddress
import java.time.Instant

actual open class KubernetesInfoRetriever {

    private val log = LoggerFactory.getLogger(KubernetesInfoRetriever::class.java) // TODO: use ErrorLogger


    actual suspend fun retrieveKubernetesInfo(): KubernetesInfo? {
        try {
            val namespaceFile = File("/run/secrets/kubernetes.io/serviceaccount/namespace")
            if (namespaceFile.exists() == false) {
                log.info("Not running in a Kubernetes environment, file '/run/secrets/kubernetes.io/serviceaccount/namespace' does not exist.")
                return null
            }

            val namespace = namespaceFile.readText()

            val localHost = InetAddress.getLocalHost()
            val podName = localHost.hostName
            val podIp = localHost.hostAddress

            return retrieveKubernetesInfo(namespace, podName, podIp)
        } catch(e: Throwable) {
            log.error("Could not retrieve any pod / Kubernetes info, no cluster or container info will be added to logs", e)
        }

        return null
    }

    open fun retrieveKubernetesInfo(namespace: String, podName: String, podIp: String): KubernetesInfo? {
        return try {
            val client = KubernetesClientBuilder().build()//.adapt(OpenShiftClient::class.java)
            // disable HttpClient's logging, is really very verbose
            disableVerboseKubernetesClientLogging(client)

            val pod = client.pods().inNamespace(namespace).withName(podName)?.get()

            retrieveKubernetesInfo(pod, namespace, podName, podIp)
        } catch(e: Throwable) {
            log.error("Does the pod have the privilege to access the Kubernetes API (see https://github.com/codinux-gmbh/ElasticsearchLogger#kubernetes-info)? " +
                    "Could not retrieve pod info, only basic data like Kubernetes namespace and Pod name are added to logs, but no cluster or container info", e)

            KubernetesInfo(namespace, podName, podIp, Instant.now().toString())
        }
    }

    protected open fun retrieveKubernetesInfo(pod: Pod?, namespace: String, podName: String, podIp: String): KubernetesInfo? {
        var containerName: String? = null
        var podIp = podIp
        var nodeIp: String? = null
        var nodeName: String? = null
        var startTime: String = DataConfig.getCurrentTimeFormatted()

        var containerId: String? = null
        var imageName: String? = null
        var imageId: String? = null
        var restartCount = 0
        var uid: String? = null

        var labels: Map<String, String> = mapOf()
        var annotations: Map<String, String> = mapOf()

        pod?.apply {
            metadata?.let { metadata ->
                uid = metadata.uid // a string like 2e8e9f1e-7df3-48d4-a0fa-908b042bc63b // TODO: needed?

                labels = metadata.labels ?: mapOf()
                annotations = metadata.annotations ?: mapOf()
            }

            spec?.let { podSpec ->
                nodeName = podSpec.nodeName

                // TODO: find the container we are running in. Till then we simply take the first container
                podSpec.containers?.firstOrNull()?.let { container ->
                    containerName = container.name
                    imageName = container.image // TODO: extract imageName and version from "docker.dankito.net/dankito/whats-on-the-radio:1.0.0-beta1"
                }
            }

            status?.let { podStatus ->
                podIp = podStatus.podIP
                nodeIp = podStatus.hostIP
                startTime = podStatus.startTime

                // TODO: find the container we are running in. Till then we simply take the first container
                podStatus.containerStatuses?.firstOrNull()?.let { containerStatus ->
                    containerName = containerStatus.name
                    containerId = containerStatus.containerID // TODO: extract containerId from "containerd://4da3e2d01401819e6a8637171eb1637f7f2e970d0b3ccca50f40b126637d9d2d" docker://694c2546560bd5b05789b6b53ac2f25941003feff12ff00fdfe68384dac4f334
                    imageName = containerStatus.image // TODO: extract imageName and version from "docker.dankito.net/dankito/whats-on-the-radio:1.0.0-beta1"
                    imageId = containerStatus.imageID // TODO: extract imageName and containerHash from "docker.dankito.net/dankito/whats-on-the-radio@sha256:5c080729130b99faf113fef9f3430e811d0fd8f13a40169745afeeda3c482ded"
                    restartCount = containerStatus.restartCount
                }
            }
        }

        return KubernetesInfo(namespace, podName, podIp, startTime, uid, restartCount, containerName, containerId,
            imageName, imageId, nodeIp, nodeName, labels, annotations)
    }

    private fun disableVerboseKubernetesClientLogging(client: KubernetesClient) {
        try {
            val httpClient = client.httpClient
            if (httpClient is OkHttpClientImpl) {
                httpClient.okHttpClient.networkInterceptors().filterIsInstance<HttpLoggingInterceptor>()
                    .forEach { it.level = HttpLoggingInterceptor.Level.NONE }
            }
        } catch (e: Throwable) {
            log.error("Could not disable verbose logging of Kubernetes Client", e)
        }
    }

}