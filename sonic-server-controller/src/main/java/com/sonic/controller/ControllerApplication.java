package com.sonic.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author ZhouYiXun
 * @des 控制中心启动类
 * @date 2021/8/15 19:56
 */
@SpringBootApplication(scanBasePackages = {"com/sonic/controller", "com/sonic/common"})
@EnableJpaAuditing
@EnableEurekaClient
public class ControllerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ControllerApplication.class, args);
    }
}
