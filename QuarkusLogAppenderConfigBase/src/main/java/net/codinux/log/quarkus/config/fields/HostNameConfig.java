package net.codinux.log.quarkus.config.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.LogAppenderFieldsConfig;

@ConfigGroup
public interface HostNameConfig {

    /**
     * If the host name field should be included in Elasticsearch index.
     */
    @WithDefault(LogAppenderFieldsConfig.IncludeHostNameDefaultValueString)
    boolean include();

    /**
     * The name of the host name field.
     */
    @WithName("fieldname")
    @WithDefault(LogAppenderFieldsConfig.HostNameDefaultFieldName)
    String fieldName();

}