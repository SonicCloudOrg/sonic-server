package org.cloud.sonic.controller;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author ZhouYiXun
 * @des 控制中心启动类
 * @date 2021/8/15 19:56
 */
@SpringBootApplication
@MapperScan(basePackages = {
        "org.cloud.sonic.controller.mapper",
        "com.gitee.sunchenbin.mybatis.actable.dao.*"
})
@ComponentScan(basePackages = {
        "org.cloud.sonic.controller.*",
        "com.gitee.sunchenbin.mybatis.actable.manager.*",
        "org.cloud.sonic.common.*"
})
@EnableFeignClients
@EnableEurekaClient
public class ControllerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ControllerApplication.class, args);
    }
}
