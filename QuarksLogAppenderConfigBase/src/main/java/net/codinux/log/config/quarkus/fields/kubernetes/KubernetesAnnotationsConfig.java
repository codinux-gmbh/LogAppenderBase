package net.codinux.log.config.quarkus.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConvertWith;

import net.codinux.log.LogAppenderConfig;
import net.codinux.log.config.quarkus.converter.FieldNamePrefixConverter;

@ConfigGroup
public class KubernetesAnnotationsConfig {

    /**
     * If Kubernetes annotations should be included in Elasticsearch index.
     */
    @ConfigItem(defaultValue = LogAppenderConfig.IncludeKubernetesAnnotationsDefaultValueString)
    public boolean include;

    /**
     * Sets a prefix for all Kubernetes annotations. Defaults to "annotation".
     *
     * Empty string or special value "off" turns prefix off.
     */
    @ConfigItem(defaultValue = LogAppenderConfig.KubernetesAnnotationsPrefixDefaultValue)
    @ConvertWith(FieldNamePrefixConverter.class)
    public String prefix;

}