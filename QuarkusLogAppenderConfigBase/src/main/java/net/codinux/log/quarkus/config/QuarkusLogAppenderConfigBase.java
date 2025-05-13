package net.codinux.log.quarkus.config;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.LogAppenderConfig;
import net.codinux.log.config.WriterConfig;
import net.codinux.log.quarkus.config.fields.QuarkusLogAppenderFieldsConfig;

import java.time.Duration;
import java.util.Optional;

@RegisterForReflection
public interface QuarkusLogAppenderConfigBase {

    /**
     * If logging to Elasticsearch should be enabled or not.
     */
    @WithDefault(LogAppenderConfig.EnabledDefaultValueString)
    boolean enable();

    /**
     * The logger name under which the internal state and errors get logged.
     */
    Optional<String> stateLoggerName();

    /**
     * URL of the endpoint where to reach ElasticSearch / Loki (e.g. http://localhost:9200 / http://localhost:3100).
     */
    @WithName("host-url")
    String hostUrl();

    /**
     * For password protected Loki instances, to Username to authenticate to Loki.
     */
    Optional<String> username();

    /**
     * For password protected Loki instances, to Password to authenticate to Loki.
     */
    Optional<String> password();


    /**
     * Config for logged fields.
     */
    @WithName("field")
    QuarkusLogAppenderFieldsConfig fields();


    /**
     * The maximum number of log records that are send in one batch to Elasticsearch.
     */
    @WithDefault("" + WriterConfig.MaxLogRecordsPerBatchDefaultValue)
    int maxLogRecordsPerBatch();

    /**
     * The maximum number of log records being buffered before the get dropped and therefore irrevocably get lost.
     */
    @WithDefault("" + WriterConfig.MaxBufferedLogRecordsDefaultValue)
    int maxBufferedLogRecords();

    /**
     * The interval in which log records get send to Elasticsearch in milliseconds.
     */
    @WithDefault("" + WriterConfig.SendLogRecordsPeriodMillisDefaultValue)
    int sendLogRecordsPeriodMillis();

    /**
     * Sets the connection timeout, that is the time period in which a client should establish a connection with a server.
     */
    Optional<Duration> connectTimeout();

    /**
     * Sets the request timeout, that is the time period required to process an HTTP call: from sending a request to receiving a response..
     */
    Optional<Duration> requestTimeout();

//    /**
//     * To not flood logs with errors like ConnectionException internal errors get logged only once per a configurable period (default: once per 30 minutes).
//     * Accepted formats are based on the ISO-8601 duration format. The format for the value is "PnDTnHnMn.nS" where "nDT" means "n" number of
//     * Days, "nH" means "n" number of Hours, "nM" means "n" number of Minutes and "nS" means "n" number of Seconds.
//     */
//    @ConfigItem(defaultValue = QuarkusElasticsearchLogHandler.PERIOD_TO_LOG_ERRORS_DEFAULT_STRING)
//    public String periodToLogErrors;

}