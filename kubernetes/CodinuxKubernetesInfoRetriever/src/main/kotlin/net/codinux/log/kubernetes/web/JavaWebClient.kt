package net.codinux.log.kubernetes.web

import kotlinx.coroutines.future.await
import kotlinx.serialization.json.Json
import net.codinux.log.kubernetes.model.Pod
import net.codinux.log.statelogger.AppenderStateLogger
import net.codinux.log.statelogger.StdOutStateLogger
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

open class JavaWebClient(
    accessToken: String,
    kubeApiCertificate: String? = null,
    protected val stateLogger: AppenderStateLogger = StdOutStateLogger()
) : WebClient {

    protected val client = HttpClient.newBuilder()
        .sslContext(CertificateTrustManager.createSslContextForCertificate(kubeApiCertificate, stateLogger))
        .build()

    protected val requestBuilder = HttpRequest.newBuilder()
        .GET()
        .header("Authorization", "Bearer $accessToken")

    protected val json = Json {
        ignoreUnknownKeys = true
    }


    override suspend fun get(url: String): WebClientResponse = try {
        val request = requestBuilder.uri(URI.create(url)).build()

        val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).await()
        val responseBody = response.body()

        val pod: Pod? = if (response.statusCode() in 200..299 && responseBody != null) json.decodeFromString(responseBody)
                        else null

        WebClientResponse(response.statusCode(), pod)
    } catch (e: Exception) {
        stateLogger.error("Could not retrieve $url", e)

        WebClientResponse(-1, null, e)
    }

}