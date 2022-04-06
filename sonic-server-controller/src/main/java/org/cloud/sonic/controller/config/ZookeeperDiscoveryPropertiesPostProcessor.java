package org.cloud.sonic.controller.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryProperties;
import org.springframework.cloud.zookeeper.discovery.configclient.ZookeeperDiscoveryClientConfigServiceBootstrapConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * 修复开启配置中心client后，zookeeper会关闭注册的问题 {@link ZookeeperDiscoveryClientConfigServiceBootstrapConfiguration}
 *
 * @author JayWenStar
 * @date 2022/4/6 12:38 上午
 */
@Configuration
public class ZookeeperDiscoveryPropertiesPostProcessor implements BeanPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperDiscoveryPropertiesPostProcessor.class);
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ZookeeperDiscoveryProperties) {
            ZookeeperDiscoveryProperties properties = (ZookeeperDiscoveryProperties) bean;
            properties.setRegister(true);
            logger.info("Zookeeper discovery properties: {}", properties);
        }
        return bean;
    }
}
