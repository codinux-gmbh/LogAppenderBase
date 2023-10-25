package net.codinux.log.quarkus.config.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import net.codinux.log.config.KubernetesFieldsConfig;

@ConfigGroup
public class ImageIdConfig {

    /**
     * If the image id should be included in index.
     */
    @ConfigItem(defaultValue = KubernetesFieldsConfig.IncludeImageIdDefaultValueString)
    public boolean include;

    /**
     * The name of the image id index field.
     */
    @ConfigItem(name = "fieldname", defaultValue = KubernetesFieldsConfig.ImageIdDefaultFieldName)
    public String fieldName;

}