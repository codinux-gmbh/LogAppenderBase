package net.codinux.log.quarkus.config.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.KubernetesFieldsConfig;

@ConfigGroup
public interface NodeIpConfig {

    /**
     * If the node IP address should be included in index.
     */
    @WithDefault(KubernetesFieldsConfig.IncludeNodeIpDefaultValueString)
    boolean include();

    /**
     * The name of the node IP address index field.
     */
    @WithName("fieldname")
    @WithDefault(KubernetesFieldsConfig.NodeIpDefaultFieldName)
    String fieldName();

}