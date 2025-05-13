package net.codinux.log.quarkus.config.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.LogAppenderFieldsConfig;

@ConfigGroup
public interface LoggerNameConfig {

    /**
     * If the full qualified name of the logger should be included in Elasticsearch.
     */
    @WithDefault(LogAppenderFieldsConfig.IncludeLoggerNameDefaultValueString)
    boolean include();

    /**
     * The name of the logger field (that is the full qualified logger name which includes in most cases the package names).
     */
    @WithName("fieldname")
    @WithDefault(LogAppenderFieldsConfig.LoggerNameDefaultFieldName)
    String fieldName();

}