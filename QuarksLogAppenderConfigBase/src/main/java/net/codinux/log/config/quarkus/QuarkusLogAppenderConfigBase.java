package net.codinux.log.config.quarkus;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.RegisterForReflection;
import net.codinux.log.LogAppenderConfig;
import net.codinux.log.config.quarkus.fields.*;
import net.codinux.log.config.quarkus.fields.kubernetes.KubernetesInfoConfig;

@RegisterForReflection
public class QuarkusLogAppenderConfigBase {

    /**
     * If logging to Elasticsearch should be enabled or not.
     */
    @ConfigItem(defaultValue = LogAppenderConfig.EnabledDefaultValueString)
    public boolean enable;

    /**
     * URL of the endpoint where to reach ElasticSearch / Loki (e.g. http://localhost:9200 / http://localhost:3100).
     */
    @ConfigItem(name = "host-url")
    public String hostUrl;

    /**
     * For password protected Loki instances, to Username to authenticate to Loki.
     */
    @ConfigItem(defaultValue = LogAppenderConfig.UsernameNotSetString)
    public String username = null;

    /**
     * For password protected Loki instances, to Password to authenticate to Loki.
     */
    @ConfigItem(defaultValue = LogAppenderConfig.PasswordNotSetString)
    public String password = null;

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


    /**
     * The maximum number of log records that are send in one batch to Elasticsearch.
     */
    @ConfigItem(defaultValue = "" + LogAppenderConfig.MaxLogRecordsPerBatchDefaultValue)
    public int maxLogRecordsPerBatch;

    /**
     * The maximum number of log records being buffered before the get dropped and therefore irrevocably get lost.
     */
    @ConfigItem(defaultValue = "" + LogAppenderConfig.MaxBufferedLogRecordsDefaultValue)
    public int maxBufferedLogRecords;

    /**
     * The interval in which log records get send to Elasticsearch in milliseconds.
     */
    @ConfigItem(defaultValue = "" + LogAppenderConfig.SendLogRecordsPeriodMillisDefaultValue)
    public int sendLogRecordsPeriodMillis;

//    /**
//     * The logger name under which Elasticsearch Logger logs its internal errors.
//     */
//    @ConfigItem(defaultValue = QuarkusElasticsearchLogHandler.ERROR_LOGGER_DEFAULT_NAME)
//    public String errorLoggerName;
//
//    /**
//     * To not flood logs with errors like ConnectionException internal errors get logged only once per a configurable period (default: once per 30 minutes).
//     * Accepted formats are based on the ISO-8601 duration format. The format for the value is "PnDTnHnMn.nS" where "nDT" means "n" number of
//     * Days, "nH" means "n" number of Hours, "nM" means "n" number of Minutes and "nS" means "n" number of Seconds.
//     */
//    @ConfigItem(defaultValue = QuarkusElasticsearchLogHandler.PERIOD_TO_LOG_ERRORS_DEFAULT_STRING)
//    public String periodToLogErrors;

}