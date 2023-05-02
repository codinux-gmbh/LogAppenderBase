package net.codinux.log.data

import net.codinux.log.statelogger.AppenderStateLogger

actual open class ProcessDataRetriever actual constructor(
    protected open val stateLogger: AppenderStateLogger
) {

    actual open fun retrieveProcessData(): ProcessData =
        ProcessData(null, null, null) // TODO

}