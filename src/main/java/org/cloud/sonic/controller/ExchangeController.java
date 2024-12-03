/*
 *   sonic-server  Sonic Cloud Real Machine Platform.
 *   Copyright (C) 2022 SonicCloudOrg
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.cloud.sonic.controller.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.Session;
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

@Tag(name = "调度相关")
@RestController
@RequestMapping("/exchange")
@Slf4j
public class ExchangeController {

    @Autowired
    private AgentsService agentsService;
    @Autowired
    private DevicesService devicesService;

    @WebAspect
    @Operation(summary = "重启设备", description = "根据 id 重启特定设备")
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
    @Operation(summary = "下线agent", description = "下线指定的 agent")
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
