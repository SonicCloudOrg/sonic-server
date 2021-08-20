package com.sonic.fold;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages = {"com/sonic/fold", "com/sonic/common"})
@EnableEurekaClient
public class FoldApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoldApplication.class, args);
    }
}