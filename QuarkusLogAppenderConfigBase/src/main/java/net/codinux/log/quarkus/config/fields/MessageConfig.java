package net.codinux.log.quarkus.config.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import net.codinux.log.config.LogAppenderFieldsConfig;

@ConfigGroup
public class MessageConfig {

    /**
     * The name of the message field.
     */
    @ConfigItem(name = "fieldname", defaultValue = LogAppenderFieldsConfig.MessageDefaultFieldName)
    public String fieldName;

}