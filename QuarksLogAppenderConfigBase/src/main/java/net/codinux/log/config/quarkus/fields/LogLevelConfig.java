package net.codinux.log.config.quarkus.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

import net.codinux.log.LogAppenderConfig;

@ConfigGroup
public class LogLevelConfig {

    /**
     * If the log level field should be included in Elasticsearch index.
     */
    @ConfigItem(defaultValue = LogAppenderConfig.IncludeLogLevelDefaultValueString)
    public boolean include;

    /**
     * The name of the log level field.
     */
    @ConfigItem(name = "fieldname", defaultValue = LogAppenderConfig.LogLevelDefaultFieldName)
    public String fieldName;

}