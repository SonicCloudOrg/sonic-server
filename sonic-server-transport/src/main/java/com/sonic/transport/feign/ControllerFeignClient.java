package com.sonic.transport.feign;

import com.sonic.transport.feign.fallBack.ControllerFeignClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "sonic-server-controller", fallback = ControllerFeignClientFallBack.class)
public interface ControllerFeignClient {
}