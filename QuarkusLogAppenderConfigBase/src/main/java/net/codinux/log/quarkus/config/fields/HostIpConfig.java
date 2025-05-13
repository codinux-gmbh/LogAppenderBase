package net.codinux.log.quarkus.config.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.LogAppenderFieldsConfig;

@ConfigGroup
public interface HostIpConfig {

    /**
     * If the host IP field should be included in Elasticsearch index.
     */
    @WithDefault(LogAppenderFieldsConfig.IncludeHostIpDefaultValueString)
    boolean include();

    /**
     * The name of the host IP field.
     */
    @WithName("fieldname")
    @WithDefault(LogAppenderFieldsConfig.HostIpDefaultFieldName)
    String fieldName();

}