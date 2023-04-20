package net.codinux.log.config.quarkus.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

import net.codinux.log.LogAppenderConfig;

@ConfigGroup
public class StacktraceConfig {

    /**
     * If the stacktrace field should be included in Elasticsearch index.
     */
    @ConfigItem(defaultValue = LogAppenderConfig.IncludeStacktraceDefaultValueString)
    public boolean include;

    /**
     * The name of the stacktrace field.
     */
    @ConfigItem(name = "fieldname", defaultValue = LogAppenderConfig.StacktraceDefaultFieldName)
    public String fieldName;

    /**
     * The name of the stacktrace field.
     */
    @ConfigItem(name = "maxFieldLength", defaultValue = LogAppenderConfig.StacktraceMaxFieldLengthDefaultValueString)
    public int maxFieldLength;

}