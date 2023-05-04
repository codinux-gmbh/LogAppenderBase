package net.codinux.log.data

expect class GZipEncoder() {

    fun gzip(body: Any?): ByteArray?

}