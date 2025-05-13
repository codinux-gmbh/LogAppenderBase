package net.codinux.log.quarkus.config.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.LogAppenderFieldsConfig;

@ConfigGroup
public interface AppVersionConfig {

    /**
     * If the app version field should be included in Elasticsearch index.
     */
    @WithDefault(LogAppenderFieldsConfig.IncludeAppVersionDefaultValueString)
    boolean include();

    /**
     * The name of the app version field.
     */
    @WithName("fieldname")
    @WithDefault(LogAppenderFieldsConfig.AppVersionDefaultFieldName)
    String fieldName();

    /**
     * The application version as it gets sent to logging backend.
     *
     * Defaults to ${quarkus.application.version} (see <a href="https://quarkus.io/guides/all-config#quarkus-core_quarkus.application.version">Quarkus config quarkus.application.version</a>).
     */
    @WithName("version")
    @WithDefault("${quarkus.application.version}")
    String appVersion();

}