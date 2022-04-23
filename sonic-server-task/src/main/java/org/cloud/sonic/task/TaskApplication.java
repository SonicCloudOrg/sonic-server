package org.cloud.sonic.task;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan(basePackages = {
        "org.cloud.sonic.task.mapper",
        "com.gitee.sunchenbin.mybatis.actable.dao.*"
})
@ComponentScan(basePackages = {
        "org.cloud.sonic.task.*",
        "com.gitee.sunchenbin.mybatis.actable.manager.*",
        "org.cloud.sonic.common.*"
})
@EnableEurekaClient
@EnableFeignClients
@EnableScheduling
public class TaskApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskApplication.class, args);
    }
}
