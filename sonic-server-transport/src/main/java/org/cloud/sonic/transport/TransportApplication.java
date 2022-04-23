package org.cloud.sonic.transport;

import org.cloud.sonic.transport.tools.SpringTool;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = {"org/cloud/sonic/transport", "org/cloud/sonic/common"})
@EnableEurekaClient
@EnableFeignClients
@Import(SpringTool.class)
public class TransportApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransportApplication.class, args);
    }
}