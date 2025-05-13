package net.codinux.log.quarkus.config.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.KubernetesFieldsConfig;

@ConfigGroup
public interface StartTimeConfig {

    /**
     * If the container start time should be included in index.
     */
    @WithDefault(KubernetesFieldsConfig.IncludeStartTimeDefaultValueString)
    boolean include();

    /**
     * The name of the container start time index field.
     */
    @WithName("fieldname")
    @WithDefault(KubernetesFieldsConfig.StartTimeDefaultFieldName)
    String fieldName();

}