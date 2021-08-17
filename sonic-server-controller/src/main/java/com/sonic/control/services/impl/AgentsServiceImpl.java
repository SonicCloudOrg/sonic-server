package com.sonic.control.services.impl;

import com.sonic.control.dao.AgentsRepository;
import com.sonic.control.dao.DevicesRepository;
import com.sonic.control.models.Agents;
import com.sonic.control.models.Devices;
import com.sonic.control.models.interfaces.AgentStatus;
import com.sonic.control.models.interfaces.DeviceStatus;
import com.sonic.control.services.AgentsService;
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
    public void save(Agents agents) {
        agentsRepository.save(agents);
    }

    @Override
    public Agents findTopByName(String name) {
        return agentsRepository.findTopByName(name);
    }

    @Override
    public boolean statusChange(int id) {
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
