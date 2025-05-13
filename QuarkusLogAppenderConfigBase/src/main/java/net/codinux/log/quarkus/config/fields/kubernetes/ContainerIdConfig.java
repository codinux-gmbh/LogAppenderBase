package net.codinux.log.quarkus.config.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.KubernetesFieldsConfig;

@ConfigGroup
public interface ContainerIdConfig {

    /**
     * If the container id should be included in index.
     */
    @WithDefault(KubernetesFieldsConfig.IncludeContainerIdDefaultValueString)
    boolean include();

    /**
     * The name of the container id index field.
     */
    @WithName("fieldname")
    @WithDefault(KubernetesFieldsConfig.ContainerIdDefaultFieldName)
    String fieldName();

}