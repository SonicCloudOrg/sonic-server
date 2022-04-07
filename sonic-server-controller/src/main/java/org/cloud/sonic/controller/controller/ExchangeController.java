/**
 *  Copyright (C) [SonicCloudOrg] Sonic Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.cloud.sonic.controller.controller;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.feign.ControllerFeignClient;
import org.cloud.sonic.common.http.RespModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.cloud.sonic.controller.netty.NettyServer;
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
