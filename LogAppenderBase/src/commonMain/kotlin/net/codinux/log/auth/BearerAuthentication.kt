package net.codinux.log.auth

open class BearerAuthentication(
    val bearerToken: String,
) : Authentication {
    override fun toString() = "Bearer authentication with token $bearerToken"
}