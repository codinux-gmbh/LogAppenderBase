package net.codinux.log.quarkus.config.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.KubernetesFieldsConfig;

@ConfigGroup
public interface NodeNameConfig {

    /**
     * If the node name should be included in index.
     */
    @WithDefault(KubernetesFieldsConfig.IncludeNodeNameDefaultValueString)
    boolean include();

    /**
     * The name of the node name index field.
     */
    @WithName("fieldname")
    @WithDefault(KubernetesFieldsConfig.NodeNameDefaultFieldName)
    String fieldName();

}