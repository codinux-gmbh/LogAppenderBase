package net.codinux.log.quarkus.config.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithConverter;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import net.codinux.log.config.LogAppenderFieldsConfig;
import net.codinux.log.quarkus.converter.FieldNamePrefixConverter;

@ConfigGroup
public interface KubernetesInfoConfig {

    /**
     * If Pod and Kubernetes info should be included in Elasticsearch index.
     */
    @WithDefault(LogAppenderFieldsConfig.IncludeKubernetesInfoDefaultValueString)
    boolean include();

    /**
     * Sets a prefix for all Kubernetes info keys. Defaults to "k8s".
     *
     * Empty string or special value "off" turns prefix off.
     */
    @WithDefault(LogAppenderFieldsConfig.KubernetesFieldsPrefixDefaultValue)
    @WithConverter(FieldNamePrefixConverter.class)
    String prefix();


    /**
     * Config for logged Kubernetes fields.
     */
    @WithName("field")
    QuarkusKubernetesFieldsConfig fields();

}