package org.cloud.sonic.controller.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
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

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AgentsServiceImpl extends SonicServiceImpl<AgentsMapper, Agents> implements AgentsService  {

    @Autowired
    private DevicesService devicesService;
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
            agents.setHost("未知");
            agents.setStatus(AgentStatus.OFFLINE);
            agents.setVersion("未知");
            agents.setPort(0);
            agents.setSystemType("未知");
            agents.setSecretKey(UUID.randomUUID().toString());
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
    public boolean offLine(int id) {
        if (existsById(id)) {
            Agents agentOffLine = findById(id);
            agentOffLine.setStatus(AgentStatus.OFFLINE);
            save(agentOffLine);
            resetDevice(agentOffLine.getId());
            return true;
        } else {
            return false;
        }
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
}
