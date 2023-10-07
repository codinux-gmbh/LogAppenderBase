package net.codinux.log.config.quarkus.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import net.codinux.log.config.KubernetesFieldsConfig;

@ConfigGroup
public class ContainerNameConfig {

    /**
     * If the container name should be included in index.
     */
    @ConfigItem(defaultValue = KubernetesFieldsConfig.IncludeContainerNameDefaultValueString)
    public boolean include;

    /**
     * The name of the container name index field.
     */
    @ConfigItem(name = "fieldname", defaultValue = KubernetesFieldsConfig.ContainerNameDefaultFieldName)
    public String fieldName;

}