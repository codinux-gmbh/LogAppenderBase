package net.codinux.log.quarkus.config.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.LogAppenderFieldsConfig;

@ConfigGroup
public interface AppNameConfig {

    /**
     * If the app name field should be included in Elasticsearch index.
     */
    @WithDefault(LogAppenderFieldsConfig.IncludeAppNameDefaultValueString)
    boolean include();

    /**
     * The name of the app name field.
     */
    @WithName("fieldname")
    @WithDefault(LogAppenderFieldsConfig.AppNameDefaultFieldName)
    String fieldName();

    /**
     * The application name as it gets sent to logging backend.
     *
     * Defaults to ${quarkus.application.name} (see <a href="https://quarkus.io/guides/all-config#quarkus-core_quarkus.application.name">Quarkus config quarkus.application.name</a>).
     */
    @WithName("name")
    @WithDefault("${quarkus.application.name}")
    String appName();

}