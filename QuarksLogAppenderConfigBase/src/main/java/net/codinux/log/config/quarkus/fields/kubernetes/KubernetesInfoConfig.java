package net.codinux.log.config.quarkus.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConvertWith;
import net.codinux.log.LogAppenderFieldsConfig;
import net.codinux.log.config.quarkus.converter.FieldNamePrefixConverter;

@ConfigGroup
public class KubernetesInfoConfig {

    /**
     * If Pod and Kubernetes info should be included in Elasticsearch index.
     */
    @ConfigItem(defaultValue = LogAppenderFieldsConfig.IncludeKubernetesInfoDefaultValueString)
    public boolean include;

    /**
     * Sets a prefix for all Kubernetes info keys. Defaults to "k8s".
     *
     * Empty string or special value "off" turns prefix off.
     */
    @ConfigItem(defaultValue = LogAppenderFieldsConfig.KubernetesFieldsPrefixDefaultValue)
    @ConvertWith(FieldNamePrefixConverter.class)
    public String prefix;

    public KubernetesLabelsConfig labels;

    public KubernetesAnnotationsConfig annotations;

}