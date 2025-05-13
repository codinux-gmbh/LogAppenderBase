package net.codinux.log.quarkus.config.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.KubernetesFieldsConfig;

@ConfigGroup
public interface ImageIdConfig {

    /**
     * If the image id should be included in index.
     */
    @WithDefault(KubernetesFieldsConfig.IncludeImageIdDefaultValueString)
    boolean include();

    /**
     * The name of the image id index field.
     */
    @WithName("fieldname")
    @WithDefault(KubernetesFieldsConfig.ImageIdDefaultFieldName)
    String fieldName();

}