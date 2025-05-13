package net.codinux.log.quarkus.config.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.KubernetesFieldsConfig;

@ConfigGroup
public interface ImageNameConfig {

    /**
     * If the image name should be included in index.
     */
    @WithDefault(KubernetesFieldsConfig.IncludeImageNameDefaultValueString)
    boolean include();

    /**
     * The name of the image name index field.
     */
    @WithName("fieldname")
    @WithDefault(KubernetesFieldsConfig.ImageNameDefaultFieldName)
    String fieldName();

}