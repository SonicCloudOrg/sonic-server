/**
 * Copyright (C) [SonicCloudOrg] Sonic Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cloud.sonic.controller.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.config.WhiteUrl;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.models.domain.Agents;
import org.cloud.sonic.controller.models.domain.Devices;
import org.cloud.sonic.controller.models.interfaces.AgentStatus;
import org.cloud.sonic.controller.services.AgentsService;
import org.cloud.sonic.controller.services.DevicesService;
import org.cloud.sonic.controller.tools.BytesTool;
import org.cloud.sonic.controller.transport.TransportWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.websocket.Session;


@Api(tags = "调度相关")
@RestController
@RequestMapping("/exchange")
@Slf4j
public class ExchangeController {

    @Autowired
    private AgentsService agentsService;
    @Autowired
    private DevicesService devicesService;

    @WebAspect
    @ApiOperation(value = "重启设备", notes = "根据 id 重启特定设备")
    @GetMapping("/reboot")
    public RespModel<String> reboot(@RequestParam(name = "id") int id) {

        Devices devices = devicesService.findById(id);
        Agents agents = agentsService.findById(devices.getAgentId());
        if (ObjectUtils.isEmpty(agents)) {
            return new RespModel<>(RespEnum.AGENT_NOT_ONLINE);
        }
        if (ObjectUtils.isEmpty(devices)) {
            return new RespModel<>(RespEnum.DEVICE_NOT_FOUND);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", "reboot");
        jsonObject.put("udId", devices.getUdId());
        jsonObject.put("platform", devices.getPlatform());
        TransportWorker.send(agents.getId(), jsonObject);
        return new RespModel<>(RespEnum.HANDLE_OK);
    }

    @WebAspect
    @ApiOperation(value = "下线agent", notes = "下线指定的 agent")
    @GetMapping("/stop")
    public RespModel<String> stop(@RequestParam(name = "id") int id) {
        Agents agents = agentsService.findById(id);
        if (agents.getStatus() != AgentStatus.ONLINE) {
            return new RespModel<>(2000, "stop.agent.not.online");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", "shutdown");
        TransportWorker.send(agents.getId(), jsonObject);
        return new RespModel<>(RespEnum.HANDLE_OK);
    }

    //eureka调度用
    @WebAspect
    @WhiteUrl
    @PostMapping("/send")
    public RespModel<String> send(@RequestParam(name = "id") int id, @RequestBody JSONObject jsonObject) {
        Session agentSession = BytesTool.agentSessionMap.get(id);
        if (agentSession != null) {
            BytesTool.sendText(agentSession, jsonObject.toJSONString());
        }
        return new RespModel<>(RespEnum.SEND_OK);
    }
}
