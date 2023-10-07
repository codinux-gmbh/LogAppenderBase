package net.codinux.log.quarkus.config.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import net.codinux.log.config.LogAppenderFieldsConfig;

@ConfigGroup
public class AppVersionConfig {

    /**
     * If the app version field should be included in Elasticsearch index.
     */
    @ConfigItem(defaultValue = LogAppenderFieldsConfig.IncludeAppVersionDefaultValueString)
    public boolean include;

    /**
     * The name of the app version field.
     */
    @ConfigItem(name = "fieldname", defaultValue = LogAppenderFieldsConfig.AppVersionDefaultFieldName)
    public String fieldName;

    /**
     * The application version as it gets sent to logging backend.
     *
     * Defaults to ${quarkus.application.version} (see <a href="https://quarkus.io/guides/all-config#quarkus-core_quarkus.application.version">Quarkus config quarkus.application.version</a>).
     */
    @ConfigItem(name = "version", defaultValue = "${quarkus.application.version}")
    public String appVersion;

}