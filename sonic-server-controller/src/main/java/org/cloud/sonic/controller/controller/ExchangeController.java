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

import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.models.domain.Agents;
import org.cloud.sonic.controller.models.domain.Devices;
import org.cloud.sonic.controller.models.interfaces.AgentStatus;
import org.cloud.sonic.controller.services.AgentsService;
import org.cloud.sonic.controller.services.DevicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exchange")
@Slf4j
public class ExchangeController {

    @Autowired
    private AgentsService agentsService;
    @Autowired
    private DevicesService devicesService;

    @WebAspect
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

//        Address address = new Address(agents.getHost() + "", agents.getRpcPort());
//        RpcContext.getContext().setObjectAttachment("address", address);
//        boolean deviceOnline;
//        try {
//            deviceOnline = agentsClientService.checkDeviceOnline(devices.getUdId(), devices.getPlatform());
//        } catch (Exception e) {
//            deviceOnline = false;
//        }
//        if (!deviceOnline) {
//            return new RespModel<>(2001, "reboot.not.online");
//        }
//
//        RpcContext.getContext().setObjectAttachment("address", address);
//        try {
//            Boolean reboot = agentsClientService.reboot(devices.getUdId(), devices.getPlatform());
//            if (reboot) {
//                return new RespModel<>(RespEnum.HANDLE_OK);
//            }
//            return new RespModel<>(2001, "reboot.device.not.found");
//        } catch (Exception e) {
//            log.error("Reboot device fail, cause :", e);
        return new RespModel<>(2001, "reboot.error.unknown");
//        }
    }

    @WebAspect
    @GetMapping("/stop")
    public RespModel<String> stop(@RequestParam(name = "id") int id) {
        Agents agents = agentsService.findById(id);
        if (agents.getStatus() != AgentStatus.ONLINE) {
            return new RespModel<>(2000, "stop.agent.not.online");
        }
//        try {
//            Address address = new Address(agents.getHost() + "", agents.getRpcPort());
//            RpcContext.getContext().setObjectAttachment("address", address);
//            agentsClientService.stop();
//        } catch (Exception e) {
//            log.error("Stop agent fail, cause :", e);
//            return new RespModel<>(RespEnum.UNKNOWN_ERROR);
//        }
        return new RespModel<>(RespEnum.HANDLE_OK);
    }
}
