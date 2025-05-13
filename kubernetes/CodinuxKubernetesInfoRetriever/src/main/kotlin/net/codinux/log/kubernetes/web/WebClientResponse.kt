package net.codinux.log.kubernetes.web

import net.codinux.log.kubernetes.model.Pod

data class WebClientResponse(
    val statusCode: Int,
    val responseBody: Pod? = null,
    val error: Throwable? = null
) {
    val isSuccess: Boolean
        get() = statusCode in 200..299
}