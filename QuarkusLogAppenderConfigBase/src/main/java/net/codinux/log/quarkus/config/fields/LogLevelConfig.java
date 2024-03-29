package net.codinux.log.quarkus.config.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import net.codinux.log.config.LogAppenderFieldsConfig;

@ConfigGroup
public class LogLevelConfig {

    /**
     * If the log level field should be included in Elasticsearch index.
     */
    @ConfigItem(defaultValue = LogAppenderFieldsConfig.IncludeLogLevelDefaultValueString)
    public boolean include;

    /**
     * The name of the log level field.
     */
    @ConfigItem(name = "fieldname", defaultValue = LogAppenderFieldsConfig.LogLevelDefaultFieldName)
    public String fieldName;

}