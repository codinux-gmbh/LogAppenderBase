package net.codinux.log.data

actual class KtorStreamContent actual constructor(content: Any) {

    actual companion object {

        actual val isSupported = false

        actual val additionalHeaders = emptyMap<String, String>()

    }

}