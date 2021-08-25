package com.sonic.transport.controller;

import com.alibaba.fastjson.JSONObject;
import com.sonic.common.config.WebAspect;
import com.sonic.common.http.RespModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transport")
public class TransportController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @WebAspect
    @GetMapping("/reboot")
    public RespModel reboot(@RequestParam(name = "platform") int platform,
                            @RequestParam(name = "udId") String udId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", "reboot");
        jsonObject.put("udId", udId);
        jsonObject.put("platform", platform);
        rabbitTemplate.convertAndSend("AgentExchange", null, jsonObject);
        return new RespModel(2000, "发送成功！");
    }

    @WebAspect
    @PostMapping("/sendTestData")
    public RespModel sendTestData(@RequestBody JSONObject jsonObject) {
        rabbitTemplate.convertAndSend("AgentExchange", null, jsonObject);
        return new RespModel(2000, "发送成功！");
    }
}
