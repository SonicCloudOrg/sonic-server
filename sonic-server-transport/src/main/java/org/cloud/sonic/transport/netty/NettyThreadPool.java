package org.cloud.sonic.transport.netty;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class NettyThreadPool {
    public static ExecutorService cachedThreadPool;

    @Bean
    public void nettyMsgInit() {
        cachedThreadPool = Executors.newCachedThreadPool();
    }
}