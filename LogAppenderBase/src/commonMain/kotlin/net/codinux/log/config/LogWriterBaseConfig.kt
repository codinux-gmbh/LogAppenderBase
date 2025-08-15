package net.codinux.log.config

open class LogWriterBaseConfig(
    open val isEnabled: Boolean,
    open val costlyFields: CostlyFieldsConfig,

    open val writerConfig: WriterConfig = WriterConfig(),
    open val logsKubernetesFields: Boolean = false,
) {
    override fun toString() = "Is enabled? $isEnabled"
}