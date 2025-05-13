package net.codinux.log.quarkus.config.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithName;
import net.codinux.log.quarkus.config.fields.kubernetes.KubernetesInfoConfig;

@ConfigGroup
public interface QuarkusLogAppenderFieldsConfig {

    MessageConfig message();

    /**
     * Config for the log level.
     */
    @WithName("level")
    LogLevelConfig logLevel();

    @WithName("loggername")
    LoggerNameConfig loggerName();

    /**
     * Config for the logger name.
     */
    @WithName("loggerclass")
    LoggerClassNameConfig loggerClassName();

    /**
     * Config for the host name.
     */
    @WithName("hostname")
    HostNameConfig hostName();

    /**
     * Config for the host IP.
     */
    @WithName("hostip")
    HostIpConfig hostIp();

    /**
     * Config for the app name.
     */
    @WithName("app")
    AppNameConfig appName();

    /**
     * Config for the app version.
     */
    @WithName("version")
    AppVersionConfig appVersion();

    /**
     * Config for the job name.
     */
    @WithName("job")
    JobNameConfig jobName();

    /**
     * Config for the thread name.
     */
    @WithName("threadname")
    ThreadNameConfig threadName();

    StacktraceConfig stacktrace();


    MdcConfig mdc();

    MarkerConfig marker();

    NdcConfig ndc();


    /**
     * Configure which Kubernetes values to include in log.
     */
    @WithName("kubernetes")
    KubernetesInfoConfig kubernetesInfo();

}