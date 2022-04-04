package org.cloud.sonic.controller;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.zookeeper.discovery.configclient.ZookeeperDiscoveryClientConfigServiceBootstrapConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author ZhouYiXun
 * @des 控制中心启动类
 * @date 2021/8/15 19:56
 */
@SpringBootApplication(scanBasePackages = {
        "org.cloud.sonic.controller",
        "com.gitee.sunchenbin.mybatis.actable.manager",
        "org.cloud.sonic.common"
})
@MapperScan(basePackages = {
        "org.cloud.sonic.controller.mapper",
        "com.gitee.sunchenbin.mybatis.actable.dao"
})
@DubboComponentScan(basePackages = {"org.cloud.sonic.controller.services.impl"})
@EnableFeignClients(basePackages = {"org.cloud.sonic.common.feign"})
@EnableDiscoveryClient
@EnableScheduling
public class ControllerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ControllerApplication.class, args);
    }
}
