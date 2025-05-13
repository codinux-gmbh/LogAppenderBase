package net.codinux.log.quarkus.config.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.LogAppenderFieldsConfig;

@ConfigGroup
public interface ThreadNameConfig {

    /**
     * If the thread name field should be included in Elasticsearch index.
     */
    @WithDefault(LogAppenderFieldsConfig.IncludeThreadNameDefaultValueString)
    boolean include();

    /**
     * The name of the thread name field.
     */
    @WithName("fieldname")
    @WithDefault(LogAppenderFieldsConfig.ThreadNameDefaultFieldName)
    String fieldName();

}