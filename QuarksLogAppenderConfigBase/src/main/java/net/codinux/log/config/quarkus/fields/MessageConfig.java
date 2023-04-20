package net.codinux.log.config.quarkus.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

import net.codinux.log.LogAppenderConfig;

@ConfigGroup
public class MessageConfig {

    /**
     * The name of the message field.
     */
    @ConfigItem(name = "fieldname", defaultValue = LogAppenderConfig.MessageDefaultFieldName)
    public String fieldName;

}