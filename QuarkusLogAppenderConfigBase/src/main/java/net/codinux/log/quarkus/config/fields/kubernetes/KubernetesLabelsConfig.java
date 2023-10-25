package net.codinux.log.quarkus.config.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConvertWith;
import net.codinux.log.config.KubernetesFieldsConfig;
import net.codinux.log.quarkus.converter.FieldNamePrefixConverter;

@ConfigGroup
public class KubernetesLabelsConfig {

    /**
     * If Kubernetes labels should be included in Elasticsearch index.
     */
    @ConfigItem(defaultValue = KubernetesFieldsConfig.IncludeLabelsDefaultValueString)
    public boolean include;

    /**
     * Sets a prefix for all Kubernetes labels. Defaults to "label".
     *
     * Empty string or special value "off" turns prefix off.
     */
    @ConfigItem(defaultValue = KubernetesFieldsConfig.LabelsPrefixDefaultValue)
    @ConvertWith(FieldNamePrefixConverter.class)
    public String prefix;

}