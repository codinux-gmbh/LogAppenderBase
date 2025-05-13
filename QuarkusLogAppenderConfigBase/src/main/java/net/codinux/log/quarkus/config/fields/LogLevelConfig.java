package net.codinux.log.quarkus.config.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.LogAppenderFieldsConfig;

@ConfigGroup
public interface LogLevelConfig {

    /**
     * If the log level field should be included in Elasticsearch index.
     */
    @WithDefault(LogAppenderFieldsConfig.IncludeLogLevelDefaultValueString)
    boolean include();

    /**
     * The name of the log level field.
     */
    @WithName("fieldname")
    @WithDefault(LogAppenderFieldsConfig.LogLevelDefaultFieldName)
    String fieldName();

}