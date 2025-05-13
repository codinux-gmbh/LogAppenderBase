package net.codinux.log.quarkus.config.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.LogAppenderFieldsConfig;

@ConfigGroup
public interface MarkerConfig {

    /**
     * If Marker should be included in Elasticsearch index.
     */
    @WithDefault(LogAppenderFieldsConfig.IncludeMarkerDefaultValueString)
    boolean include();

    /**
     * The name of the Marker field, defaults to "marker".
     */
    @WithName("fieldname")
    @WithDefault(LogAppenderFieldsConfig.MarkerDefaultFieldName)
    String fieldName();

}