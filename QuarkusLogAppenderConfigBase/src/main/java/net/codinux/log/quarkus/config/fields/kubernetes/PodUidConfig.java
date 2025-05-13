package net.codinux.log.quarkus.config.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.KubernetesFieldsConfig;

@ConfigGroup
public interface PodUidConfig {

    /**
     * If the Pod UID should be included in index.
     */
    @WithDefault(KubernetesFieldsConfig.IncludePodUidDefaultValueString)
    boolean include();

    /**
     * The name of the Pod UID index field.
     */
    @WithName("fieldname")
    @WithDefault(KubernetesFieldsConfig.PodUidDefaultFieldName)
    String fieldName();

}