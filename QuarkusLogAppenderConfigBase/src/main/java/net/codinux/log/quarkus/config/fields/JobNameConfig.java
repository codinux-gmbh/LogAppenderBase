package net.codinux.log.quarkus.config.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.LogAppenderFieldsConfig;

@ConfigGroup
public interface JobNameConfig {

    /**
     * If the job name field should be included in Elasticsearch index (for most applications this value shouldn't be of any interest).
     */
    @WithDefault(LogAppenderFieldsConfig.IncludeJobNameDefaultValueString)
    boolean include();

    /**
     * The name of the job name field.
     */
    @WithName("fieldname")
    @WithDefault(LogAppenderFieldsConfig.JobNameDefaultFieldName)
    String fieldName();

    /**
     * The job name as it gets sent to logging backend. Is interesting to differ it from jobs of log collectors like
     * fluentd and K8sLogCollector.
     */
    @WithName("name")
    @WithDefault(LogAppenderFieldsConfig.JobNameDefaultValueString)
    String jobName();

}