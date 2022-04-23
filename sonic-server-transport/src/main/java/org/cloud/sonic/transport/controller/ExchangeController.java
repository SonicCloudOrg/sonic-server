package org.cloud.sonic.transport.controller;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.transport.feign.ControllerFeignClient;
import org.cloud.sonic.transport.netty.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {
    @Autowired
    private ControllerFeignClient controllerFeignClient;

    @WebAspect
    @GetMapping("/reboot")
    public RespModel reboot(@RequestParam(name = "id") int id) {
        RespModel device = controllerFeignClient.findDeviceById(id);
        if (device.getCode() == 2000) {
            LinkedHashMap d = (LinkedHashMap) device.getData();
            RespModel agent = controllerFeignClient.findAgentById((Integer) d.get("agentId"));
            if (agent.getCode() == 2000) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("msg", "reboot");
                jsonObject.put("udId", d.get("udId"));
                jsonObject.put("platform", d.get("platform"));
                LinkedHashMap a = (LinkedHashMap) agent.getData();
                NettyServer.getMap().get(a.get("id")).writeAndFlush(jsonObject.toJSONString());
                return new RespModel(2000, "发送成功！");
            } else {
                return agent;
            }
        } else {
            return device;
        }
    }

    @WebAspect
    @GetMapping("/stop")
    public RespModel<String> stop(@RequestParam(name = "id") int id) {
        RespModel agent = controllerFeignClient.findAgentById(id);
        if (agent.getCode() == 2000) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", "stop");
            LinkedHashMap a = (LinkedHashMap) agent.getData();
            NettyServer.getMap().get(a.get("id")).writeAndFlush(jsonObject.toJSONString());
            return new RespModel<>(2000, "发送成功！");
        } else {
            return agent;
        }
    }

    @WebAspect
    @PostMapping("/sendTestData")
    public RespModel sendTestData(@RequestBody JSONObject jsonObject) {
        if (jsonObject.getInteger("id") != null) {
            NettyServer.getMap().get(jsonObject.getInteger("id")).writeAndFlush(jsonObject.toJSONString());
        }
        return new RespModel(2000, "发送成功！");
    }
}
