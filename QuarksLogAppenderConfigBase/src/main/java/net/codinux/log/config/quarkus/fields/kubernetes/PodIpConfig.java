package net.codinux.log.config.quarkus.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import net.codinux.log.config.KubernetesFieldsConfig;

@ConfigGroup
public class PodIpConfig {

    /**
     * If the Pod IP address should be included in index.
     */
    @ConfigItem(defaultValue = KubernetesFieldsConfig.IncludePodIpDefaultValueString)
    public boolean include;

    /**
     * The name of the Pod IP address index field.
     */
    @ConfigItem(name = "fieldname", defaultValue = KubernetesFieldsConfig.PodIpDefaultFieldName)
    public String fieldName;

}