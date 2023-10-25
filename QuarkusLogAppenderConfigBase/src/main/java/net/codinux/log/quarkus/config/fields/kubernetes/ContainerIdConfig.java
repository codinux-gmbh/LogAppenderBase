package net.codinux.log.quarkus.config.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import net.codinux.log.config.KubernetesFieldsConfig;

@ConfigGroup
public class ContainerIdConfig {

    /**
     * If the container id should be included in index.
     */
    @ConfigItem(defaultValue = KubernetesFieldsConfig.IncludeContainerIdDefaultValueString)
    public boolean include;

    /**
     * The name of the container id index field.
     */
    @ConfigItem(name = "fieldname", defaultValue = KubernetesFieldsConfig.ContainerIdDefaultFieldName)
    public String fieldName;

}