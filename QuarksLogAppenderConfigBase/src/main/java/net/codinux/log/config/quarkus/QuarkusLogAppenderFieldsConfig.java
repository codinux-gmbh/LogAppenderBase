package net.codinux.log.config.quarkus;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import net.codinux.log.config.quarkus.fields.*;
import net.codinux.log.config.quarkus.fields.kubernetes.KubernetesInfoConfig;

@ConfigGroup
public class QuarkusLogAppenderFieldsConfig {

    public MessageConfig message;

    /**
     * Config for the log level.
     */
    @ConfigItem(name = "level")
    public LogLevelConfig logLevel;

    @ConfigItem(name = "loggername")
    public LoggerNameConfig loggerName;

    /**
     * Config for the logger name.
     */
    @ConfigItem(name = "loggerclass")
    public LoggerClassNameConfig loggerClassName;

    /**
     * Config for the host name.
     */
    @ConfigItem(name = "hostname")
    public HostNameConfig hostName;

    /**
     * Config for the host IP.
     */
    @ConfigItem(name = "hostip")
    public HostNameConfig hostIp;

    /**
     * Config for the app name.
     */
    @ConfigItem(name = "app")
    public AppNameConfig appName;

    /**
     * Config for the thread name.
     */
    @ConfigItem(name = "threadname")
    public ThreadNameConfig threadName;

    public StacktraceConfig stacktrace;


    public MdcConfig mdc;

    public MarkerConfig marker;

    public NdcConfig ndc;


    /**
     * Configure which Kubernetes values to include in log.
     */
    @ConfigItem(name = "kubernetes")
    public KubernetesInfoConfig kubernetesInfo;

}