package net.codinux.log.kubernetes.model

import kotlinx.serialization.Serializable

@Serializable
data class Container(
    val name: String,
    val image: String? = null,
    val imagePullPolicy: ImagePullPolicy? = null
) {

    override fun toString(): String {
        return name
    }

}