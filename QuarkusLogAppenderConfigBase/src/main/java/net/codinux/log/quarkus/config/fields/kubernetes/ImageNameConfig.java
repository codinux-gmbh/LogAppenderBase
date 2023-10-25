package net.codinux.log.quarkus.config.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import net.codinux.log.config.KubernetesFieldsConfig;

@ConfigGroup
public class ImageNameConfig {

    /**
     * If the image name should be included in index.
     */
    @ConfigItem(defaultValue = KubernetesFieldsConfig.IncludeImageNameDefaultValueString)
    public boolean include;

    /**
     * The name of the image name index field.
     */
    @ConfigItem(name = "fieldname", defaultValue = KubernetesFieldsConfig.ImageNameDefaultFieldName)
    public String fieldName;

}