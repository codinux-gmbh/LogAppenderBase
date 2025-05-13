package net.codinux.log.quarkus.config.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.LogAppenderFieldsConfig;

@ConfigGroup
public interface NdcConfig {

    /**
     * If NDC (Nested Diagnostic Context) should be included in Elasticsearch index.
     */
    @WithDefault(LogAppenderFieldsConfig.IncludeNdcDefaultValueString)
    boolean include();

    /**
     * The name of the NDC field, defaults to "ndc".
     */
    @WithName("fieldname")
    @WithDefault(LogAppenderFieldsConfig.NdcDefaultFieldName)
    String fieldName();

}