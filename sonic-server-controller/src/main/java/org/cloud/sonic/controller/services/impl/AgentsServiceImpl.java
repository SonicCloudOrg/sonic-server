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

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.controller.mapper.AgentsMapper;
import org.cloud.sonic.controller.models.domain.Agents;
import org.cloud.sonic.controller.models.domain.Devices;
import org.cloud.sonic.controller.models.interfaces.AgentStatus;
import org.cloud.sonic.controller.models.interfaces.DeviceStatus;
import org.cloud.sonic.controller.services.AgentsService;
import org.cloud.sonic.controller.services.DevicesService;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.cloud.sonic.controller.tools.RobotMsgTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AgentsServiceImpl extends SonicServiceImpl<AgentsMapper, Agents> implements AgentsService {

    @Autowired
    private DevicesService devicesService;
    @Autowired
    private RobotMsgTool robotMsgTool;
    @Resource
    private AgentsMapper agentsMapper;

    @Override
    public List<Agents> findAgents() {
        return list();
    }

    @Override
    public void update(int id, String name, int highTemp, int highTempTime, int robotType, String robotToken, String robotSecret) {
        if (id == 0) {
            Agents agents = new Agents();
            agents.setName(name);
            agents.setHost("unknown");
            agents.setStatus(AgentStatus.OFFLINE);
            agents.setVersion("unknown");
            agents.setPort(0);
            agents.setSystemType("unknown");
            agents.setHighTemp(highTemp);
            agents.setHighTempTime(highTempTime);
            agents.setRobotType(robotType);
            agents.setRobotToken(robotToken);
            agents.setRobotSecret(robotSecret);
            agents.setSecretKey(UUID.randomUUID().toString());
            save(agents);
        } else {
            Agents ag = findById(id);
            if (ObjectUtils.isNotEmpty(ag)) {
                ag.setName(name);
                ag.setHighTemp(highTemp);
                ag.setHighTempTime(highTempTime);
                ag.setRobotType(robotType);
                ag.setRobotToken(robotToken);
                ag.setRobotSecret(robotSecret);
                save(ag);
            }
        }
    }

    public void resetDevice(int id) {
        List<Devices> devicesList = devicesService.listByAgentId(id);
        for (Devices devices : devicesList) {
            if ((!devices.getStatus().equals(DeviceStatus.OFFLINE))
                    && (!devices.getStatus().equals(DeviceStatus.DISCONNECTED))) {
                devices.setStatus(DeviceStatus.OFFLINE);
                devicesService.save(devices);
            }
        }
    }

    @Override
    public void saveAgents(JSONObject jsonObject) {
        if (jsonObject.getInteger("agentId") != null && jsonObject.getInteger("agentId") != 0) {
            if (existsById(jsonObject.getInteger("agentId"))) {
                Agents oldAgent = findById(jsonObject.getInteger("agentId"));
                oldAgent.setStatus(AgentStatus.ONLINE);
                oldAgent.setHost(jsonObject.getString("host"));
                oldAgent.setPort(jsonObject.getInteger("port"));
                oldAgent.setVersion(jsonObject.getString("version"));
                oldAgent.setSystemType(jsonObject.getString("systemType"));
                save(oldAgent);
            }
        }
    }

    @Override
    public void saveAgents(Agents agents) {
        save(agents);
    }

    @Override
    @Transactional
    public boolean updateAgentsByLockVersion(Agents agents) {
        return lambdaUpdate()
                .eq(Agents::getId, agents.getId())
                .eq(Agents::getLockVersion, agents.getLockVersion())
                .update(agents.setLockVersion(agents.getLockVersion() + 1));
    }

    @Deprecated
    @Override
    public boolean offLine(int id) {
        if (existsById(id)) {
            Agents agentOffLine = findById(id);
            agentOffLine
                    .setStatus(AgentStatus.OFFLINE);
            updateAgentsByLockVersion(agentOffLine);
            resetDevice(agentOffLine.getId());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Agents auth(String key) {
        Agents agents = findBySecretKey(key);
        if (agents != null) {
            resetDevice(agents.getId());
        }
        return agents;
    }

    @Override
    public Agents findById(int id) {
        return baseMapper.selectById(id);
    }

    @Override
    public Agents findBySecretKey(String secretKey) {
        return lambdaQuery().eq(Agents::getSecretKey, secretKey).one();
    }

    @Override
    public void errCall(int id, String udId, int tem, int type) {
        Agents agents = findById(id);
        if (agents != null && agents.getRobotType() != 0 && agents.getRobotToken().length() > 0 && agents.getRobotSecret().length() > 0) {
            robotMsgTool.sendErrorDevice(agents.getRobotToken(), agents.getRobotSecret(), agents.getRobotType(), type, tem, udId);
        }
    }
}
