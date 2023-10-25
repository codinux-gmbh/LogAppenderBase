package net.codinux.log.quarkus.config.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import net.codinux.log.config.KubernetesFieldsConfig;

@ConfigGroup
public class PodUidConfig {

    /**
     * If the Pod UID should be included in index.
     */
    @ConfigItem(defaultValue = KubernetesFieldsConfig.IncludePodUidDefaultValueString)
    public boolean include;

    /**
     * The name of the Pod UID index field.
     */
    @ConfigItem(name = "fieldname", defaultValue = KubernetesFieldsConfig.PodUidDefaultFieldName)
    public String fieldName;

}