package net.codinux.log.data

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.ktor.http.content.*
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import java.util.zip.GZIPOutputStream

actual class KtorStreamContent actual constructor(private val content: Any) : OutgoingContent.WriteChannelContent() {

    actual companion object {

        actual val isSupported = true

        actual val additionalHeaders = mapOf("Content-Encoding" to "gzip")

    }


    private val objectMapper = ObjectMapper().apply {
        registerModule(KotlinModule.Builder().build())
    }

    override suspend fun writeTo(channel: ByteWriteChannel) {
        val outputStream = channel.toOutputStream()

        objectMapper.writeValue(GZIPOutputStream(outputStream), content)
    }

}