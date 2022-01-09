package org.cloud.sonic.folder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages = {"org/cloud/sonic/folder", "org/cloud/sonic/common"})
@EnableEurekaClient
public class FolderApplication {
    public static void main(String[] args) {
        SpringApplication.run(FolderApplication.class, args);
    }
}