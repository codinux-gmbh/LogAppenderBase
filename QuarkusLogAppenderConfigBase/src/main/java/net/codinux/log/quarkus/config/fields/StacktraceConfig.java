package net.codinux.log.quarkus.config.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.LogAppenderFieldsConfig;

@ConfigGroup
public interface StacktraceConfig {

    /**
     * If the stacktrace field should be included in Elasticsearch index.
     */
    @WithDefault(LogAppenderFieldsConfig.IncludeStacktraceDefaultValueString)
    boolean include();

    /**
     * The name of the stacktrace field.
     */
    @WithName("fieldname")
    @WithDefault(LogAppenderFieldsConfig.StacktraceDefaultFieldName)
    String fieldName();

    /**
     * The name of the stacktrace field.
     */
    @WithName("maxFieldLength")
    @WithDefault(LogAppenderFieldsConfig.StacktraceMaxFieldLengthDefaultValueString)
    int maxFieldLength();

}