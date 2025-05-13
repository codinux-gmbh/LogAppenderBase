package net.codinux.log.quarkus.config.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.LogAppenderFieldsConfig;

@ConfigGroup
public interface LoggerClassNameConfig {

    /**
     * In most cases the logger is a full qualified class name including the package names.
     * If Elasticsearch logger should try to extract the class name - that is without package name - of the logger and include it in Elasticsearch index.
     */
    @WithDefault(LogAppenderFieldsConfig.IncludeLoggerClassNameDefaultValueString)
    boolean include();

    /**
     * In most cases the logger is a full qualified class name including the package names.
     * Elasticsearch logger can try to extract class' name from full qualified logger and log this.
     * This is the field name for this.
     */
    @WithName("fieldname")
    @WithDefault(LogAppenderFieldsConfig.LoggerClassNameDefaultFieldName)
    String fieldName();

}