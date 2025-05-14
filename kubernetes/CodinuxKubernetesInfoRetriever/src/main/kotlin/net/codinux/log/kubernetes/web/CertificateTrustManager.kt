package net.codinux.log.kubernetes.web

import net.codinux.log.statelogger.AppenderStateLogger
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object CertificateTrustManager {

    // fallback trust store if Kube server certificate is not mounted to pod
    val TrustAllCertificatesTrustManager = object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate?> = arrayOf()
        override fun checkClientTrusted(certs: Array<X509Certificate?>?, authType: String?) { }
        override fun checkServerTrusted(certs: Array<X509Certificate?>?, authType: String?) {
            // trust all certificates
        }
    }


    fun createSslContextForCertificate(certificateString: String?, stateLogger: AppenderStateLogger): SSLContext =
        SSLContext.getInstance("TLS").apply {
            this.init(null, arrayOf(createTrustManagerForCertificate(certificateString, stateLogger)), null)
        }

    fun createTrustManagerForCertificate(certificateString: String?, stateLogger: AppenderStateLogger): TrustManager {
        try {
            if (certificateString != null) {
                val certificateFactory = CertificateFactory.getInstance("X.509")
                val certificate = certificateFactory.generateCertificate(certificateString.byteInputStream())

                return createTrustManagerForCertificate(certificate)
            }
        } catch (e: Throwable) { // should never occur
            stateLogger.error("Could not add Kubernetes server certificate to KeyStore", e)
        }

        return TrustAllCertificatesTrustManager
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

        return trustManagerFactory.trustManagers.filterIsInstance<X509TrustManager>().first()
    }

}