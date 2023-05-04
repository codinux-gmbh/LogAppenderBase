package net.codinux.log.kubernetes

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import net.codinux.log.kubernetes.model.Pod
import net.codinux.log.statelogger.AppenderStateLogger
import net.codinux.log.statelogger.StdOutStateLogger
import java.io.File
import java.net.InetAddress
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class CodinuxKubernetesInfoRetriever(
    private val stateLogger: AppenderStateLogger = StdOutStateLogger()
) : KubernetesInfoRetriever {

    companion object {
        private val KubernetesSecretsFolder = File("/run/secrets/kubernetes.io/serviceaccount/")
    }


    // fallback trust store if Kube server certificate is not mounted to pod
    private val trustAllCertificatesTrustManager = object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate?> = arrayOf()
        override fun checkClientTrusted(certs: Array<X509Certificate?>?, authType: String?) { }
        override fun checkServerTrusted(certs: Array<X509Certificate?>?, authType: String?) {
            // trust all certificates
        }
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
            val host = System.getProperty("KUBERNETES_SERVICE_HOST")
            val port = System.getProperty("KUBERNETES_SERVICE_PORT_HTTPS")

            val apiServer = if (host != null && port != null) "https://$host:$port"
                            else "kubernetes.default.svc"
            val url = "$apiServer/api/v1/namespaces/$namespace/pods/$podName"

            val client = createClientForCertificate(kubeApiCertificate)

            val response = client.get(url) {
                bearerAuth(accessToken)
            }

            if (response.status.isSuccess()) {
                return mapPodResponse(response.body(), namespace, podName, podIp)
            } else {
                if (response.status == HttpStatusCode.Forbidden) {
                    stateLogger.error("Access to Pod information is forbidden. Did you add the privilege to read pod information to your pod's ServiceAccount? (see )")
                } else {
                    stateLogger.error("Could not retrieve Pod information: ${response.status} ${response.bodyAsText()}")
                }
            }
        } catch (e: Throwable) {
            stateLogger.error("Could not retrieve Pod information", e)
        }

        return PodInfo(namespace, podName, podIp)
    }

    private fun createClientForCertificate(certificate: String?): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
            engine {
                https {
                    trustManager = createTrustManagerForCertificate(certificate)
                }
            }
        }
    }

    private fun createTrustManagerForCertificate(certificateString: String?): TrustManager {
        try {
            if (certificateString != null) {
                val certificateFactory = CertificateFactory.getInstance("X.509")
                val certificate = certificateFactory.generateCertificate(certificateString.byteInputStream())

                return createTrustManagerForCertificate(certificate)
            }
        } catch (e: Throwable) { // should never occur
            stateLogger.error("Could not add Kubernetes server certificate to KeyStore: $e")
        }

        return trustAllCertificatesTrustManager
    }

    private fun createTrustManagerForCertificate(certificate: Certificate): X509TrustManager {
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
            this.load(null) // not really needed

            this.setCertificateEntry("KubeServer", certificate) // use a random name?
        }

        return createTrustManagerForKeyStore(keyStore)
    }

    private fun createTrustManagerForKeyStore(keyStore: KeyStore): X509TrustManager {
        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
            this.init(keyStore)
        }

        val sslContext = SSLContext.getInstance("TLS").apply {
            this.init(null, trustManagerFactory.trustManagers, null)
        }

        return trustManagerFactory.trustManagers.filterIsInstance<X509TrustManager>().first()
    }

    private fun mapPodResponse(pod: Pod?, namespace: String, podName: String, podIp: String) = pod?.let {
        // TODO: find the container we are running in. Till then we simply take the first container
        val containerStatus = pod.status.containerStatuses.firstOrNull()
        val container = pod.spec.containers.firstOrNull()

        PodInfo(
            pod.metadata.namespace.ifBlank { namespace },
            pod.metadata.name.ifBlank { podName },
            pod.status.podIP ?: podIp,
            pod.status.startTime, // TODO: get current time
            pod.metadata.uid,
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