package net.codinux.log.quarkus.config.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.KubernetesFieldsConfig;

@ConfigGroup
public interface ContainerNameConfig {

    /**
     * If the container name should be included in index.
     */
    @WithDefault(KubernetesFieldsConfig.IncludeContainerNameDefaultValueString)
    boolean include();

    /**
     * The name of the container name index field.
     */
    @WithName("fieldname")
    @WithDefault(KubernetesFieldsConfig.ContainerNameDefaultFieldName)
    String fieldName();

}