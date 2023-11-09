package net.codinux.log.quarkus.config.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import net.codinux.log.config.LogAppenderFieldsConfig;

@ConfigGroup
public class JobNameConfig {

    /**
     * If the job name field should be included in Elasticsearch index (for most applications this value shouldn't be of any interest).
     */
    @ConfigItem(defaultValue = LogAppenderFieldsConfig.IncludeJobNameDefaultValueString)
    public boolean include;

    /**
     * The name of the job name field.
     */
    @ConfigItem(name = "fieldname", defaultValue = LogAppenderFieldsConfig.JobNameDefaultFieldName)
    public String fieldName;

    /**
     * The job name as it gets sent to logging backend. Is interesting to differ it from jobs of log collectors like
     * fluentd and K8sLogCollector.
     */
    @ConfigItem(name = "name", defaultValue = LogAppenderFieldsConfig.JobNameDefaultValueString)
    public String jobName;

}