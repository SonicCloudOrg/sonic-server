package com.sonic.transport.feign;

import com.alibaba.fastjson.JSONObject;
import com.sonic.common.http.RespModel;
import com.sonic.transport.feign.fallBack.ControllerFeignClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "sonic-server-controller", fallback = ControllerFeignClientFallBack.class)
public interface ControllerFeignClient {
    @PutMapping("/devices/deviceStatus")
    RespModel deviceStatus(@RequestBody JSONObject jsonObject);

    @PutMapping("/agents")
    RespModel saveAgent(@RequestBody JSONObject agents);

    @GetMapping("/agents/offLine")
    RespModel offLine(@RequestParam(name = "id") int id);

    @PostMapping("/resultDetail")
    RespModel saveResultDetail(@RequestBody JSONObject jsonObject);

    @PutMapping("/elapsedTime")
    RespModel saveElapsed(@RequestBody JSONObject jsonObject);

    @GetMapping("/devices")
    RespModel findDeviceById(@RequestParam(name = "id") int id);
}