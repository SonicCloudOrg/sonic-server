package com.sonic.task.feign;

import com.sonic.common.http.RespModel;
import com.sonic.task.feign.fallback.ControllerFeignClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "sonic-server-controller", fallback = ControllerFeignClientFallBack.class)
public interface ControllerFeignClient {
    @GetMapping("/testSuites/runSuite")
    RespModel runSuite(@RequestParam(name = "id") int id);

    @GetMapping("/results/sendDayReport")
    RespModel sendDayReport();

    @GetMapping("/results/sendWeekReport")
    RespModel sendWeekReport();

    @GetMapping("/results/clean")
    RespModel clean(@RequestParam(name = "day") int day);
}