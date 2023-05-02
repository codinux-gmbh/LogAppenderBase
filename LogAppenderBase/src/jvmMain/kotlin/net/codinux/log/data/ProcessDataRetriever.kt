package net.codinux.log.data

import net.codinux.log.statelogger.AppenderStateLogger
import java.net.InetAddress

actual open class ProcessDataRetriever actual constructor(
    protected open val stateLogger: AppenderStateLogger
) {

    actual open fun retrieveProcessData(): ProcessData {
        val localHost = getLocalHost()

        return ProcessData(
            DataConfig.getCurrentTimeFormatted(),
            localHost?.hostName,
            localHost?.hostAddress
        )
    }

    protected open fun getLocalHost(): InetAddress? {
        return try {
            InetAddress.getLocalHost()
        } catch (getLocalHostException: Throwable) {
            try {
                InetAddress.getByName(null)
            } catch (getByName: Throwable) {
                // will that ever occur?
                stateLogger.error("Could not determine local host, host name and host IP will not be available", getLocalHostException)

                null
            }
        }
    }

}