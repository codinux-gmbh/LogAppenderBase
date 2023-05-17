package net.codinux.log.data

expect class KtorStreamContent(content: Any) {

    companion object {

        val isSupported: Boolean

        val additionalHeaders: Map<String, String>

    }

}