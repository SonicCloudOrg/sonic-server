package com.sonic.controller.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.sonic.controller.dao.AgentsRepository;
import com.sonic.controller.dao.DevicesRepository;
import com.sonic.controller.models.Agents;
import com.sonic.controller.models.Devices;
import com.sonic.controller.models.interfaces.AgentStatus;
import com.sonic.controller.models.interfaces.DeviceStatus;
import com.sonic.controller.services.AgentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentsServiceImpl implements AgentsService {
    @Autowired
    private AgentsRepository agentsRepository;
    @Autowired
    private DevicesRepository devicesRepository;

    @Override
    public List<Agents> findAgents() {
        return agentsRepository.findAll();
    }

    @Override
    public Agents findByIp(String ip) {
        return agentsRepository.findByIp(ip);
    }

    @Override
    public void save(JSONObject jsonObject) {
        if (jsonObject.getInteger("id") != null && jsonObject.getInteger("id") != 0) {
            if (agentsRepository.existsById(jsonObject.getInteger("id"))) {
                Agents oldAgent = agentsRepository.findById(jsonObject.getInteger("id")).get();
                oldAgent.setStatus(AgentStatus.ONLINE);
                oldAgent.setIp(jsonObject.getString("ip"));
                oldAgent.setPort(jsonObject.getInteger("port"));
                oldAgent.setVersion(jsonObject.getString("version"));
                oldAgent.setSystemType(jsonObject.getString("systemType"));
                agentsRepository.save(oldAgent);
            }
        }
    }

    @Override
    public Agents findTopByName(String name) {
        return agentsRepository.findTopByName(name);
    }

    @Override
    public boolean offLine(int id) {
        if (agentsRepository.existsById(id)) {
            Agents agentOffLine = agentsRepository.findById(id).get();
            agentOffLine.setStatus(AgentStatus.OFFLINE);
            agentsRepository.save(agentOffLine);
            List<Devices> devicesList = devicesRepository.findByAgentId(agentOffLine.getId());
            for (Devices devices : devicesList) {
                if ((!devices.getStatus().equals(DeviceStatus.OFFLINE))
                        && (!devices.getStatus().equals(DeviceStatus.DISCONNECTED))) {
                    devices.setStatus(DeviceStatus.OFFLINE);
                    devicesRepository.save(devices);
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
