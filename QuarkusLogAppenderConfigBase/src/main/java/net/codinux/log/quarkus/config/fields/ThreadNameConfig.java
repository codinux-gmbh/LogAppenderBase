package net.codinux.log.quarkus.config.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import net.codinux.log.config.LogAppenderFieldsConfig;

@ConfigGroup
public class ThreadNameConfig {

    /**
     * If the thread name field should be included in Elasticsearch index.
     */
    @ConfigItem(defaultValue = LogAppenderFieldsConfig.IncludeThreadNameDefaultValueString)
    public boolean include;

    /**
     * The name of the thread name field.
     */
    @ConfigItem(name = "fieldname", defaultValue = LogAppenderFieldsConfig.ThreadNameDefaultFieldName)
    public String fieldName;

}