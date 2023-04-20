package net.codinux.log.config.quarkus.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

import net.codinux.log.LogAppenderConfig;

@ConfigGroup
public class NdcConfig {

    /**
     * If NDC (Nested Diagnostic Context) should be included in Elasticsearch index.
     */
    @ConfigItem(defaultValue = LogAppenderConfig.IncludeNdcDefaultValueString)
    public boolean include;

    /**
     * The name of the NDC field, defaults to "ndc".
     */
    @ConfigItem(name = "fieldname", defaultValue = LogAppenderConfig.NdcDefaultFieldName)
    public String fieldName;

}