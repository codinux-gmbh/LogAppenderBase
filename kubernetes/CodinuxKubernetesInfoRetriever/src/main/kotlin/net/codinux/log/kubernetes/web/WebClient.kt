package net.codinux.log.kubernetes.web

interface WebClient {

    suspend fun get(url: String): WebClientResponse

}