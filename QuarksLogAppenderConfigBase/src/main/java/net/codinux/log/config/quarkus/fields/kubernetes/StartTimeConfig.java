package net.codinux.log.config.quarkus.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import net.codinux.log.config.KubernetesFieldsConfig;

@ConfigGroup
public class StartTimeConfig {

    /**
     * If the container start time should be included in index.
     */
    @ConfigItem(defaultValue = KubernetesFieldsConfig.IncludeStartTimeDefaultValueString)
    public boolean include;

    /**
     * The name of the container start time index field.
     */
    @ConfigItem(name = "fieldname", defaultValue = KubernetesFieldsConfig.StartTimeDefaultFieldName)
    public String fieldName;

}