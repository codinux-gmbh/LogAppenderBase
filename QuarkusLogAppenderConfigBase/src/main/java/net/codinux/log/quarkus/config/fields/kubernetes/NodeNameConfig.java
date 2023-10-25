package net.codinux.log.quarkus.config.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import net.codinux.log.config.KubernetesFieldsConfig;

@ConfigGroup
public class NodeNameConfig {

    /**
     * If the node name should be included in index.
     */
    @ConfigItem(defaultValue = KubernetesFieldsConfig.IncludeNodeNameDefaultValueString)
    public boolean include;

    /**
     * The name of the node name index field.
     */
    @ConfigItem(name = "fieldname", defaultValue = KubernetesFieldsConfig.NodeNameDefaultFieldName)
    public String fieldName;

}