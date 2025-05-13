package net.codinux.log.kubernetes.web

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import net.codinux.log.statelogger.AppenderStateLogger
import net.codinux.log.statelogger.StdOutStateLogger
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

open class KtorWebClient(
    accessToken: String,
    kubeApiCertificate: String? = null,
    protected val stateLogger: AppenderStateLogger = StdOutStateLogger()
) : WebClient {

    protected val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        engine {
            https {
                trustManager = createTrustManagerForCertificate(kubeApiCertificate)
            }
        }

        defaultRequest {
            bearerAuth(accessToken)
        }
    }


    // fallback trust store if Kube server certificate is not mounted to pod
    private val trustAllCertificatesTrustManager = object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate?> = arrayOf()
        override fun checkClientTrusted(certs: Array<X509Certificate?>?, authType: String?) { }
        override fun checkServerTrusted(certs: Array<X509Certificate?>?, authType: String?) {
            // trust all certificates
        }
    }


    override suspend fun get(url: String): WebClientResponse = try {
        val response = client.get(url)

        WebClientResponse(response.status.value, response.body())
    } catch (e: Exception) {
        stateLogger.error("Could not retrieve $url", e)

        WebClientResponse(-1, null, e)
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

}