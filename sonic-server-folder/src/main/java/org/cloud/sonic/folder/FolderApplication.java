package org.cloud.sonic.folder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"org.cloud.sonic.folder", "org.cloud.sonic.common"})
@EnableConfigServer
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"org.cloud.sonic.common.feign"})
public class FolderApplication {
    public static void main(String[] args) {
        SpringApplication.run(FolderApplication.class, args);
    }
}