package net.codinux.log.config.quarkus.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

import net.codinux.log.LogAppenderConfig;

@ConfigGroup
public class HostNameConfig {

    /**
     * If the host name field should be included in Elasticsearch index.
     */
    @ConfigItem(defaultValue = LogAppenderConfig.IncludeHostNameDefaultValueString)
    public boolean include;

    /**
     * The name of the host name field.
     */
    @ConfigItem(name = "fieldname", defaultValue = LogAppenderConfig.HostNameDefaultFieldName)
    public String fieldName;

}