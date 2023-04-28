package net.codinux.log.data

import java.net.InetAddress

actual class ProcessDataRetriever {

    actual fun retrieveProcessData(): ProcessData {
        val localHost = getLocalHost()

        return ProcessData(
            localHost?.hostName,
            localHost?.hostAddress
        )
    }

    private fun getLocalHost(): InetAddress? {
        return try {
            InetAddress.getLocalHost()
        } catch (getLocalHostException: Exception) {
            try {
                InetAddress.getByName(null as String?)
            } catch (getByName: Exception) {
                // TODO: log both exceptions with ErrorLogger

                null
            }
        }
    }

}