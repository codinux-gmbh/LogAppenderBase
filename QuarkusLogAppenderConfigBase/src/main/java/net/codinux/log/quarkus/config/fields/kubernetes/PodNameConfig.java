package net.codinux.log.quarkus.config.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.KubernetesFieldsConfig;

@ConfigGroup
public interface PodNameConfig {

    /**
     * If the Pod name should be included in index.
     */
    @WithDefault(KubernetesFieldsConfig.IncludePodNameDefaultValueString)
    boolean include();

    /**
     * The name of the Pod name index field.
     */
    @WithName("fieldname")
    @WithDefault(KubernetesFieldsConfig.PodNameDefaultFieldName)
    String fieldName();

}