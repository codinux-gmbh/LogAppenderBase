package net.codinux.log.config.quarkus.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import net.codinux.log.config.KubernetesFieldsConfig;

@ConfigGroup
public class RestartCountConfig {

    /**
     * If container's restart count should be included in index.
     */
    @ConfigItem(defaultValue = KubernetesFieldsConfig.IncludeRestartCountDefaultValueString)
    public boolean include;

    /**
     * The name of the container restart count index field.
     */
    @ConfigItem(name = "fieldname", defaultValue = KubernetesFieldsConfig.RestartCountDefaultFieldName)
    public String fieldName;

}