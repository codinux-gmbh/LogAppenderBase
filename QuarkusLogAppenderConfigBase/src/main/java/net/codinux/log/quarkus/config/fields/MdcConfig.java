package net.codinux.log.quarkus.config.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithConverter;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.LogAppenderFieldsConfig;
import net.codinux.log.quarkus.converter.FieldNamePrefixConverter;

@ConfigGroup
public interface MdcConfig {

    /**
     * If MDC (Mapped Diagnostic Context) should be included in Elasticsearch index.
     */
    @WithDefault(LogAppenderFieldsConfig.IncludeMdcDefaultValueString)
    boolean include();

    /**
     * Sets a prefix for all MDC keys.
     *
     * E.g. prefix is set to "mdc", then the MDC gets stored as:
     *  mdc.key_1=value_1
     *  mdc.key_2=value_2
     *
     * instead of:
     *  key_1=value_1
     *  key_2=value_2
     */
    @WithName("fieldname")
    @WithDefault(LogAppenderFieldsConfig.MdcFieldsPrefixDefaultValue)
    @WithConverter(FieldNamePrefixConverter.class)
    String prefix();

}