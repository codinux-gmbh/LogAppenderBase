package net.codinux.log.quarkus.config.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithConverter;
import io.smallrye.config.WithDefault;
import net.codinux.log.config.KubernetesFieldsConfig;
import net.codinux.log.quarkus.converter.FieldNamePrefixConverter;

@ConfigGroup
public interface KubernetesAnnotationsConfig {

    /**
     * If Kubernetes annotations should be included in Elasticsearch index.
     */
    @WithDefault(KubernetesFieldsConfig.IncludeAnnotationsDefaultValueString)
    boolean include();

    /**
     * Sets a prefix for all Kubernetes annotations. Defaults to "annotation".
     *
     * Empty string or special value "off" turns prefix off.
     */
    @WithDefault(KubernetesFieldsConfig.AnnotationsPrefixDefaultValue)
    @WithConverter(FieldNamePrefixConverter.class)
    String prefix();

}