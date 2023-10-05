package net.codinux.log.config.quarkus.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import net.codinux.log.LogAppenderFieldsConfig;

@ConfigGroup
public class AppNameConfig {

    /**
     * If the app name field should be included in Elasticsearch index.
     */
    @ConfigItem(defaultValue = LogAppenderFieldsConfig.IncludeAppNameDefaultValueString)
    public boolean include;

    /**
     * The name of the app name field.
     */
    @ConfigItem(name = "fieldname", defaultValue = LogAppenderFieldsConfig.AppNameDefaultFieldName)
    public String fieldName;

    /**
     * The name of the app name field.
     *
     * Defaults to ${quarkus.application.name} (see <a href="https://quarkus.io/guides/all-config#quarkus-core_quarkus.application.name">Quarkus config quarkus.application.name</a>).
     */
    @ConfigItem(name = "name", defaultValue = "${quarkus.application.name}")
    public String appName;

}