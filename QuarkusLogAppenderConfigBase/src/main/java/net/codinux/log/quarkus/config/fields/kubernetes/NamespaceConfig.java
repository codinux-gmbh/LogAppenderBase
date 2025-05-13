package net.codinux.log.quarkus.config.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.KubernetesFieldsConfig;

@ConfigGroup
public interface NamespaceConfig {

    /**
     * If Kubernetes namespace the Pod is running in should be included in index.
     */
    @WithDefault(KubernetesFieldsConfig.IncludeNamespaceDefaultValueString)
    boolean include();

    /**
     * The name of the Kubernetes namespace index field.
     */
    @WithName("fieldname")
    @WithDefault(KubernetesFieldsConfig.NamespaceDefaultFieldName)
    String fieldName();

}