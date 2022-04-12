package org.cloud.sonic.common.feign;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.common.feign.fallback.ControllerFeignClientFallBack;
import org.cloud.sonic.common.models.domain.Devices;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "sonic-server-controller", fallback = ControllerFeignClientFallBack.class)
public interface ControllerFeignClient {
    @PutMapping("/devices/deviceStatus")
    RespModel deviceStatus(@RequestBody JSONObject jsonObject);

    @PutMapping("/devices/updateDevicesUser")
    RespModel updateUser(@RequestBody JSONObject jsonObject);

    @PutMapping("/devices/refreshDevicesBattery")
    RespModel refreshDevicesBattery(@RequestBody JSONObject jsonObject);

    @PutMapping("/agents")
    RespModel saveAgent(@RequestBody JSONObject agents);

    @GetMapping("/agents")
    RespModel findAgentById(@RequestParam(name = "id") int id);

    @GetMapping("/agents/offLine")
    RespModel offLine(@RequestParam(name = "id") int id);

    @PostMapping("/resultDetail")
    RespModel saveResultDetail(@RequestBody JSONObject jsonObject);

    @PutMapping("/elapsedTime")
    RespModel saveElapsed(@RequestBody JSONObject jsonObject);

    @GetMapping("/devices")
    RespModel findDeviceById(@RequestParam(name = "id") int id);

    @GetMapping("/devices/listAll")
    RespModel<List<Devices>> listAll(@RequestParam(name = "platform") int platform);

    @GetMapping("/agents/findKeyById")
    RespModel findKeyById(@RequestParam(name = "id") int id);

    @GetMapping("/testCases/findSteps")
    RespModel findSteps(@RequestParam(name = "id") int id);
}