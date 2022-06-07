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
import org.cloud.sonic.controller.services.CabinetService;
import org.cloud.sonic.controller.mapper.AgentsMapper;
import org.cloud.sonic.controller.models.domain.Agents;
import org.cloud.sonic.controller.models.domain.Devices;
import org.cloud.sonic.controller.models.interfaces.AgentStatus;
import org.cloud.sonic.controller.models.interfaces.DeviceStatus;
import org.cloud.sonic.controller.services.AgentsService;
import org.cloud.sonic.controller.services.DevicesService;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class AgentsServiceImpl extends SonicServiceImpl<AgentsMapper, Agents> implements AgentsService {

    @Autowired
    private DevicesService devicesService;
    @Autowired
    private CabinetService cabinetService;
    @Resource
    private AgentsMapper agentsMapper;

    @Override
    public List<Agents> findAgents() {
        return list();
    }

    @Override
    public void updateName(int id, String name) {
        if (id == 0) {
            Agents agents = new Agents();
            agents.setName(name);
            agents.setHost("unknown");
            agents.setStatus(AgentStatus.OFFLINE);
            agents.setVersion("unknown");
            agents.setPort(0);
            agents.setSystemType("unknown");
            agents.setSecretKey(UUID.randomUUID().toString());
            agents.setCabinetId(0);
            agents.setStorey(0);
            save(agents);
        } else {
            Agents ag = findById(id);
            if (ObjectUtils.isNotEmpty(ag)) {
                ag.setName(name);
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
        if (agents.getCabinetId() != 0 && agents.getStorey() != 0) {
            Agents oldStorey = findByCabinetIdAndStorey(agents.getCabinetId(), agents.getStorey());
            if (oldStorey != null && (oldStorey.getId() != agents.getId())) {
                oldStorey.setStorey(0);
                save(oldStorey);
            }
        }
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
    public void offLine(Agents agentOffLine) {
        agentOffLine.setStatus(AgentStatus.OFFLINE);
        updateAgentsByLockVersion(agentOffLine);
        resetDevice(agentOffLine.getId());
    }

    @Override
    public int auth(String key) {
        Agents agents = findBySecretKey(key);
        if (agents == null) {
            return 0;
        } else {
            resetDevice(agents.getId());
            return agents.getId();
        }
    }

    @Override
    public String findKeyById(int id) {
        Optional<Agents> agents = lambdaQuery().eq(Agents::getId, id).select(Agents::getSecretKey).oneOpt();
        return agents.map(Agents::getSecretKey).orElse(null);
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
    public Agents findByCabinetIdAndStorey(int id, int storey) {
        return lambdaQuery().eq(Agents::getCabinetId, id)
                .eq(Agents::getStorey, storey).one();
    }

    @Override
    public List<JSONObject> findByCabinetForDetail(int id) {
        List<Agents> agentsList = lambdaQuery().eq(Agents::getCabinetId, id)
                .ne(Agents::getStorey, 0).orderByAsc(Agents::getStorey).list();
        List<JSONObject> result = new ArrayList<>();
        for (Agents agents : agentsList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("agent", agents);
            jsonObject.put("devices", devicesService.findByAgentForCabinet(agents.getId()));
            result.add(jsonObject);
        }
        return result;
    }

    @Override
    public List<Agents> findByCabinetId(int id) {
        return lambdaQuery().eq(Agents::getCabinetId, id)
                .ne(Agents::getStorey, 0).orderByAsc(Agents::getStorey).list();
    }
}
