package com.sonic.transport.controller;

import com.alibaba.fastjson.JSONObject;
import com.sonic.common.config.WebAspect;
import com.sonic.common.http.RespModel;
import com.sonic.transport.feign.ControllerFeignClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ControllerFeignClient controllerFeignClient;

    @WebAspect
    @GetMapping("/reboot")
    public RespModel reboot(@RequestParam(name = "id") int id) {
        RespModel device = controllerFeignClient.findDeviceById(id);
        if (device.getCode() == 2000) {
            LinkedHashMap d = (LinkedHashMap) device.getData();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", "reboot");
            jsonObject.put("udId", d.get("udId"));
            jsonObject.put("platform", d.get("platform"));
            jsonObject.put("agentId", d.get("agentId"));
            rabbitTemplate.convertAndSend("AgentExchange", "", jsonObject);
            return new RespModel(2000, "发送成功！");
        } else {
            return device;
        }
    }

    @WebAspect
    @PostMapping("/sendTestData")
    public RespModel sendTestData(@RequestBody JSONObject jsonObject) {
        rabbitTemplate.convertAndSend("AgentExchange", null, jsonObject);
        return new RespModel(2000, "发送成功！");
    }
}
