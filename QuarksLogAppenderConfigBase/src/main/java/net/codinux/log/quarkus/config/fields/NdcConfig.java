package net.codinux.log.quarkus.config.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import net.codinux.log.config.LogAppenderFieldsConfig;

@ConfigGroup
public class NdcConfig {

    /**
     * If NDC (Nested Diagnostic Context) should be included in Elasticsearch index.
     */
    @ConfigItem(defaultValue = LogAppenderFieldsConfig.IncludeNdcDefaultValueString)
    public boolean include;

    /**
     * The name of the NDC field, defaults to "ndc".
     */
    @ConfigItem(name = "fieldname", defaultValue = LogAppenderFieldsConfig.NdcDefaultFieldName)
    public String fieldName;

}