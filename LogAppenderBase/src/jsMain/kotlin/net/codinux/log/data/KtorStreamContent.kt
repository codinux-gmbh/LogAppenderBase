package net.codinux.log.data

actual class KtorStreamContent actual constructor(content: Any, gzipContent: Boolean) {

    actual companion object {

        actual val isSupported = false

        actual val supportsGZip = false

    }

}