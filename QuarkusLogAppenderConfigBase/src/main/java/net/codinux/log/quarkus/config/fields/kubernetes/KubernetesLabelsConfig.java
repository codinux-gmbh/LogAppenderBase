package net.codinux.log.quarkus.config.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithConverter;
import io.smallrye.config.WithDefault;
import net.codinux.log.config.KubernetesFieldsConfig;
import net.codinux.log.quarkus.converter.FieldNamePrefixConverter;

@ConfigGroup
public interface KubernetesLabelsConfig {

    /**
     * If Kubernetes labels should be included in Elasticsearch index.
     */
    @WithDefault(KubernetesFieldsConfig.IncludeLabelsDefaultValueString)
    boolean include();

    /**
     * Sets a prefix for all Kubernetes labels. Defaults to "label".
     *
     * Empty string or special value "off" turns prefix off.
     */
    @WithDefault(KubernetesFieldsConfig.LabelsPrefixDefaultValue)
    @WithConverter(FieldNamePrefixConverter.class)
    String prefix();

}