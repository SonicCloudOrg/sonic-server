package com.sonic.transport;

import com.sonic.transport.tools.SpringTool;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = {"com/sonic/transport", "com/sonic/common"})
@EnableEurekaClient
@EnableFeignClients
@Import(SpringTool.class)
public class TransportApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransportApplication.class, args);
    }
}