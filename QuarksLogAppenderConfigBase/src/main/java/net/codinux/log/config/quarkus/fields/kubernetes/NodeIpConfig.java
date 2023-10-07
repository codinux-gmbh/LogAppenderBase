package net.codinux.log.config.quarkus.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import net.codinux.log.config.KubernetesFieldsConfig;

@ConfigGroup
public class NodeIpConfig {

    /**
     * If the node IP address should be included in index.
     */
    @ConfigItem(defaultValue = KubernetesFieldsConfig.IncludeNodeIpDefaultValueString)
    public boolean include;

    /**
     * The name of the node IP address index field.
     */
    @ConfigItem(name = "fieldname", defaultValue = KubernetesFieldsConfig.NodeIpDefaultFieldName)
    public String fieldName;

}