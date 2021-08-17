package com.sonic.control.services;

import com.alibaba.fastjson.JSONObject;
import com.sonic.control.models.Agents;

import java.util.List;

public interface AgentsService {
    List<Agents> findAgents();

    Agents findByIp(String ip);

    void save(Agents agents);

    Agents findTopByName(String name);

    boolean statusChange(int id);
}