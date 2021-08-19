package com.sonic.fold;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com/sonic/fold", "com/sonic/common"})
public class FoldApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoldApplication.class, args);
    }
}