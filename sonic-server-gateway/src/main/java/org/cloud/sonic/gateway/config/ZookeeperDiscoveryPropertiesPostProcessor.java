package org.cloud.sonic.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenwenjie.star
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
