package net.codinux.log.config.quarkus.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import net.codinux.log.LogAppenderConfig;

@ConfigGroup
public class HostIpConfig {

    /**
     * If the host IP field should be included in Elasticsearch index.
     */
    @ConfigItem(defaultValue = LogAppenderConfig.IncludeHostIpDefaultValueString)
    public boolean include;

    /**
     * The name of the host IP field.
     */
    @ConfigItem(name = "fieldname", defaultValue = LogAppenderConfig.HostIpDefaultFieldName)
    public String fieldName;

}