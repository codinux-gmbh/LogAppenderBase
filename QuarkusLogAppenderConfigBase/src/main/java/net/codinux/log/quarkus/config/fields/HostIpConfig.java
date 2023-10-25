package net.codinux.log.quarkus.config.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import net.codinux.log.config.LogAppenderFieldsConfig;

@ConfigGroup
public class HostIpConfig {

    /**
     * If the host IP field should be included in Elasticsearch index.
     */
    @ConfigItem(defaultValue = LogAppenderFieldsConfig.IncludeHostIpDefaultValueString)
    public boolean include;

    /**
     * The name of the host IP field.
     */
    @ConfigItem(name = "fieldname", defaultValue = LogAppenderFieldsConfig.HostIpDefaultFieldName)
    public String fieldName;

}