package net.codinux.log.config.quarkus.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

import net.codinux.log.LogAppenderConfig;

@ConfigGroup
public class AppNameConfig {

    /**
     * If the app name field should be included in Elasticsearch index.
     */
    @ConfigItem(defaultValue = LogAppenderConfig.IncludeAppNameDefaultValueString)
    public boolean include;

    /**
     * The name of the app name field.
     */
    @ConfigItem(name = "fieldname", defaultValue = LogAppenderConfig.AppNameDefaultFieldName)
    public String fieldName;

}