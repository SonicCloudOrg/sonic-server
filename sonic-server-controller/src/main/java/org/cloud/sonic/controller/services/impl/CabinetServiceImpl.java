/*
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
package org.cloud.sonic.controller.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.controller.models.domain.Agents;
import org.cloud.sonic.controller.models.domain.Cabinet;
import org.cloud.sonic.controller.services.AgentsService;
import org.cloud.sonic.controller.services.CabinetService;
import org.cloud.sonic.controller.mapper.CabinetMapper;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.cloud.sonic.controller.tools.RobotMsgTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CabinetServiceImpl extends SonicServiceImpl<CabinetMapper, Cabinet> implements CabinetService {
    @Resource
    private CabinetMapper cabinetMapper;
    @Autowired
    private AgentsService agentsService;
    @Autowired
    private RobotMsgTool robotMsgTool;

    @Override
    public List<Cabinet> findCabinets() {
        return list();
    }

    @Override
    public void saveCabinet(Cabinet cabinet) {
        if (cabinet.getId() == 0) {
            cabinet.setSecretKey(UUID.randomUUID().toString());
        }
        save(cabinet);
        if (cabinet.getId() != 0) {
            List<Agents> agentsList = agentsService.findByCabinetId(cabinet.getId());
            for (Agents agent : agentsList) {
//                Address address = new Address(agent.getHost() + "", agent.getRpcPort());
//                RpcContext.getContext().setObjectAttachment("address", address);
//                try {
//                    agentsClientService.updateCabinetOption(cabinet);
//                } catch (Exception e) {
//                    continue;
//                }
            }
        }
    }

    @Override
    public Cabinet getIdByKey(String key) {
        return lambdaQuery().eq(Cabinet::getSecretKey, key).one();
    }

    @Override
    public void errorCall(Cabinet cabinet, String udId, int tem, int type) {
        if (cabinet.getRobotType() != 0 && cabinet.getRobotToken().length() > 0 && cabinet.getRobotSecret().length() > 0) {
            robotMsgTool.sendErrorDevice(cabinet.getRobotToken(), cabinet.getRobotSecret(), cabinet.getRobotType(), type, tem, udId);
        }
    }
}
