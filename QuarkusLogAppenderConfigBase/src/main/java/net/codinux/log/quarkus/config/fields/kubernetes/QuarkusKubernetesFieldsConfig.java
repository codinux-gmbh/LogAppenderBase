package net.codinux.log.quarkus.config.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithName;

@ConfigGroup
public interface QuarkusKubernetesFieldsConfig {

    /**
     * Config for the Kubernetes namespace the Pod is running in.
     */
    NamespaceConfig namespace();


    /**
     * Config for the Pod name index field.
     */
    @WithName("podname")
    PodNameConfig podName();

    /**
     * Config for the container name index field.
     */
    @WithName("containername")
    ContainerNameConfig containerName();

    /**
     * Config for the image name index field.
     */
    @WithName("imagename")
    ImageNameConfig imageName();


    /**
     * Config for the node name index field.
     */
    @WithName("nodename")
    NodeNameConfig nodeName();

    /**
     * Config for the node IP index field.
     */
    @WithName("nodeip")
    NodeIpConfig nodeIp();

    /**
     * Config for the POD IP index field.
     */
    @WithName("podip")
    PodIpConfig podIp();


    /**
     * Config for the container start time index field.
     */
    @WithName("starttime")
    StartTimeConfig startTime();

    /**
     * Config for the container restart count index field.
     */
    @WithName("restartcount")
    RestartCountConfig restartCount();


    /**
     * Config for the Pod UID index field.
     */
    @WithName("poduid")
    PodUidConfig podUid();

    /**
     * Config for the container id index field.
     */
    @WithName("containerid")
    ContainerIdConfig containerId();

    /**
     * Config for the image id index field.
     */
    @WithName("imageid")
    ImageIdConfig imageId();


    /**
     * Config for the Kubernetes labels index fields.
     */
    KubernetesLabelsConfig labels();


    /**
     * Config for the Kubernetes annotations index fields.
     */
    KubernetesAnnotationsConfig annotations();

}