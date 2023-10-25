package net.codinux.log.quarkus.config.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import net.codinux.log.config.KubernetesFieldsConfig;

@ConfigGroup
public class NamespaceConfig {

    /**
     * If Kubernetes namespace the Pod is running in should be included in index.
     */
    @ConfigItem(defaultValue = KubernetesFieldsConfig.IncludeNamespaceDefaultValueString)
    public boolean include;

    /**
     * The name of the Kubernetes namespace index field.
     */
    @ConfigItem(name = "fieldname", defaultValue = KubernetesFieldsConfig.NamespaceDefaultFieldName)
    public String fieldName;

}