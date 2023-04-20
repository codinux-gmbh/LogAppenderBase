package net.codinux.log.config.quarkus.fields;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

import net.codinux.log.LogAppenderConfig;

@ConfigGroup
public class MarkerConfig {

    /**
     * If Marker should be included in Elasticsearch index.
     */
    @ConfigItem(defaultValue = LogAppenderConfig.IncludeMarkerDefaultValueString)
    public boolean include;

    /**
     * The name of the Marker field, defaults to "marker".
     */
    @ConfigItem(name = "fieldname", defaultValue = LogAppenderConfig.MarkerDefaultFieldName)
    public String fieldName;

}