package net.codinux.log.config.quarkus.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

import net.codinux.log.LogAppenderConfig;

@ConfigGroup
public class LoggerNameConfig {

    /**
     * If the full qualified name of the logger should be included in Elasticsearch.
     */
    @ConfigItem(defaultValue = LogAppenderConfig.IncludeLoggerNameDefaultValueString)
    public boolean include;

    /**
     * The name of the logger field (that is the full qualified logger name which includes in most cases the package names).
     */
    @ConfigItem(name = "fieldname", defaultValue = LogAppenderConfig.LoggerNameDefaultFieldName)
    public String fieldName;

}