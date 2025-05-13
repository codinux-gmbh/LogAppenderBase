package net.codinux.log.quarkus.config.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.LogAppenderFieldsConfig;

@ConfigGroup
public interface MessageConfig {

    /**
     * The name of the message field.
     */
    @WithName("fieldname")
    @WithDefault(LogAppenderFieldsConfig.MessageDefaultFieldName)
    String fieldName();

}