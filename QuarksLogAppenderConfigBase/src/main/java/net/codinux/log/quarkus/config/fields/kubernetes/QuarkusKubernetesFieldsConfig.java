package net.codinux.log.quarkus.config.fields.kubernetes;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

@ConfigGroup
public class QuarkusKubernetesFieldsConfig {

    /**
     * Config for the Kubernetes namespace the Pod is running in.
     */
    @ConfigItem(name = "namespace")
    public NamespaceConfig namespace;


    /**
     * Config for the Pod name index field.
     */
    @ConfigItem(name = "podname")
    public PodNameConfig podName;

    /**
     * Config for the container name index field.
     */
    @ConfigItem(name = "containername")
    public ContainerNameConfig containerName;

    /**
     * Config for the image name index field.
     */
    @ConfigItem(name = "imagename")
    public ImageNameConfig imageName;


    /**
     * Config for the node name index field.
     */
    @ConfigItem(name = "nodename")
    public NodeNameConfig nodeName;

    /**
     * Config for the node IP index field.
     */
    @ConfigItem(name = "nodeip")
    public NodeIpConfig nodeIp;

    /**
     * Config for the POD IP index field.
     */
    @ConfigItem(name = "podip")
    public PodIpConfig podIp;


    /**
     * Config for the container start time index field.
     */
    @ConfigItem(name = "starttime")
    public StartTimeConfig startTime;

    /**
     * Config for the container restart count index field.
     */
    @ConfigItem(name = "restartcount")
    public RestartCountConfig restartCount;


    /**
     * Config for the Pod UID index field.
     */
    @ConfigItem(name = "poduid")
    public PodUidConfig podUid;

    /**
     * Config for the container id index field.
     */
    @ConfigItem(name = "containerid")
    public ContainerIdConfig containerId;

    /**
     * Config for the image id index field.
     */
    @ConfigItem(name = "iamgeid")
    public ImageIdConfig imageId;


    /**
     * Config for the Kubernetes labels index fields.
     */
    public KubernetesLabelsConfig labels;


    /**
     * Config for the Kubernetes annotations index fields.
     */
    public KubernetesAnnotationsConfig annotations;

}