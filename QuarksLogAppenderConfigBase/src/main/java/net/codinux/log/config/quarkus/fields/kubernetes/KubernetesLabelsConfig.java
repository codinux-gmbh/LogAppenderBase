package net.codinux.log.config.quarkus.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConvertWith;

import net.codinux.log.LogAppenderConfig;
import net.codinux.log.config.quarkus.converter.FieldNamePrefixConverter;

@ConfigGroup
public class KubernetesLabelsConfig {

    /**
     * If Kubernetes labels should be included in Elasticsearch index.
     */
    @ConfigItem(defaultValue = LogAppenderConfig.IncludeKubernetesLabelsDefaultValueString)
    public boolean include;

    /**
     * Sets a prefix for all Kubernetes labels. Defaults to "label".
     *
     * Empty string or special value "off" turns prefix off.
     */
    @ConfigItem(defaultValue = LogAppenderConfig.KubernetesLabelsPrefixDefaultValue)
    @ConvertWith(FieldNamePrefixConverter.class)
    public String prefix;

}