package net.codinux.log.data

expect class KtorStreamContent(content: Any, gzipContent: Boolean) {

    companion object {

        val isSupported: Boolean

        val supportsGZip: Boolean

    }

}