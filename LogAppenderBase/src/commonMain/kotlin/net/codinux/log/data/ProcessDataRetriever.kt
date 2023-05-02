package net.codinux.log.data

import net.codinux.log.statelogger.AppenderStateLogger

expect class ProcessDataRetriever(stateLogger: AppenderStateLogger) {

    fun retrieveProcessData(): ProcessData

}