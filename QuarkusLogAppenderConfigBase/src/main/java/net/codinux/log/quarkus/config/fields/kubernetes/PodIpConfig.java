package net.codinux.log.quarkus.config.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.KubernetesFieldsConfig;

@ConfigGroup
public interface PodIpConfig {

    /**
     * If the Pod IP address should be included in index.
     */
    @WithDefault(KubernetesFieldsConfig.IncludePodIpDefaultValueString)
    boolean include();

    /**
     * The name of the Pod IP address index field.
     */
    @WithName("fieldname")
    @WithDefault(KubernetesFieldsConfig.PodIpDefaultFieldName)
    String fieldName();

}