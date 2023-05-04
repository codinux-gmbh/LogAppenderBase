package net.codinux.log.data

import java.io.ByteArrayOutputStream
import java.util.zip.GZIPOutputStream

actual class GZipEncoder actual constructor() {

    actual fun gzip(body: Any?): ByteArray? {
        if (body is String) {
            val bytes = ByteArrayOutputStream()
            GZIPOutputStream(bytes).apply {
                write(body.toByteArray())
                close()
            }

            return bytes.toByteArray()
        }

        return null
    }

}