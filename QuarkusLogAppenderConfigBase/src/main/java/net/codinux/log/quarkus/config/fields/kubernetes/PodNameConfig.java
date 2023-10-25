package net.codinux.log.quarkus.config.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import net.codinux.log.config.KubernetesFieldsConfig;

@ConfigGroup
public class PodNameConfig {

    /**
     * If the Pod name should be included in index.
     */
    @ConfigItem(defaultValue = KubernetesFieldsConfig.IncludePodNameDefaultValueString)
    public boolean include;

    /**
     * The name of the Pod name index field.
     */
    @ConfigItem(name = "fieldname", defaultValue = KubernetesFieldsConfig.PodNameDefaultFieldName)
    public String fieldName;

}