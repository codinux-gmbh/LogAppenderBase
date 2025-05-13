package net.codinux.log.quarkus.config.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.KubernetesFieldsConfig;

@ConfigGroup
public interface RestartCountConfig {

    /**
     * If container's restart count should be included in index.
     */
    @WithDefault(KubernetesFieldsConfig.IncludeRestartCountDefaultValueString)
    boolean include();

    /**
     * The name of the container restart count index field.
     */
    @WithName("fieldname")
    @WithDefault(KubernetesFieldsConfig.RestartCountDefaultFieldName)
    String fieldName();

}