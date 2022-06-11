//package org.cloud.sonic.controller.netty;
//
//import com.netflix.appinfo.*;
//import com.netflix.discovery.DiscoveryClient;
//import com.netflix.discovery.EurekaClient;
//import org.cloud.sonic.controller.tools.PortTool;
//import org.springframework.beans.factory.SmartInitializingSingleton;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.commons.util.InetUtils;
//import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
//@Component
//public class NettyDiscoveryClient implements SmartInitializingSingleton {
//    @Value("${spring.version}")
//    private String version;
//
//    @Value("${sonic.netty.name}")
//    private String appName;
//
//    @Value("${sonic.netty.port}")
//    private int port;
//
//    @Autowired
//    private EurekaInstanceConfig config;
//
//    @Autowired
//    private InetUtils inetUtils;
//
//    @Autowired
//    private EurekaClientConfigBean eurekaClientConfigBean;
//
//    @Override
//    public void afterSingletonsInstantiated() {
//        String host = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
//        InstanceInfo instanceInfo = createInstance(config);
//        ApplicationInfoManager applicationInfoManager = new ApplicationInfoManager(new MyDataCenterInstanceConfig() {
//            @Override
//            public String getHostName(boolean refresh) {
//                return host;
//            }
//        }, instanceInfo);
//        EurekaClient eurekaClient = new DiscoveryClient(applicationInfoManager, eurekaClientConfigBean);
//    }
//
//    private InstanceInfo createInstance(EurekaInstanceConfig config) {
//
//        LeaseInfo.Builder leaseInfoBuilder = LeaseInfo.Builder.newBuilder()
//                .setRenewalIntervalInSecs(config.getLeaseRenewalIntervalInSeconds())
//                .setDurationInSecs(config.getLeaseExpirationDurationInSeconds());
//
//        // Builder the instance information to be registered with eureka
//        InstanceInfo.Builder builder = InstanceInfo.Builder.newBuilder();
//
//        String namespace = config.getNamespace();
//        if (!namespace.endsWith(".")) {
//            namespace = namespace + ".";
//        }
//
//        if(port==0){
//            port = PortTool.getPort();
//        }
//
//        String host = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
//        builder.setNamespace(namespace).setAppName(appName)
//                .setInstanceId(String.format("%s(v%s)-%s",appName, version, host))
//                .setAppGroupName(config.getAppGroupName())
//                .setDataCenterInfo(config.getDataCenterInfo())
//                .setIPAddr(host).setHostName(host)
//                .setPort(port)
//                .enablePort(InstanceInfo.PortType.UNSECURE,
//                        config.isNonSecurePortEnabled())
//                .setSecurePort(config.getSecurePort())
//                .enablePort(InstanceInfo.PortType.SECURE, config.getSecurePortEnabled())
//                .setVIPAddress(appName)
//                .setSecureVIPAddress(appName)
//                .setHomePageUrl("/", null)
//                .setStatusPageUrl(config.getStatusPageUrlPath(),
//                        config.getStatusPageUrl())
//                .setLeaseInfo(leaseInfoBuilder.build())
//                .setHealthCheckUrls(config.getHealthCheckUrlPath(),
//                        config.getHealthCheckUrl(), config.getSecureHealthCheckUrl())
//                .setASGName(config.getASGName());
//        builder.setStatus(InstanceInfo.InstanceStatus.UP);
//
//        // Add any user-specific metadata information
//        for (Map.Entry<String, String> mapEntry : config.getMetadataMap().entrySet()) {
//            String key = mapEntry.getKey();
//            String value = mapEntry.getValue();
//            // only add the metadata if the value is present
//            if (value != null && !value.isEmpty()) {
//                builder.add(key, value);
//            }
//        }
//
//        InstanceInfo instanceInfo = builder.build();
//        return instanceInfo;
//    }
//}