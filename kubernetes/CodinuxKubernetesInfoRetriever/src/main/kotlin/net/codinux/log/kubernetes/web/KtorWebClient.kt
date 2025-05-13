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
                trustManager = CertificateTrustManager.createTrustManagerForCertificate(kubeApiCertificate, stateLogger)
            }
        }

        defaultRequest {
            bearerAuth(accessToken)
        }
    }



    override suspend fun get(url: String): WebClientResponse = try {
        val response = client.get(url)

        WebClientResponse(response.status.value, response.body())
    } catch (e: Exception) {
        stateLogger.error("Could not retrieve $url", e)

        WebClientResponse(-1, null, e)
    }

}