package com.sonic.controller.services;

import com.alibaba.fastjson.JSONObject;
import com.sonic.controller.models.Agents;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des Agent逻辑层
 * @date 2021/8/19 22:51
 */
public interface AgentsService {
    List<Agents> findAgents();

    Agents findByIp(String ip);

    void save(JSONObject agents);

    Agents findTopByName(String name);

    boolean offLine(int id);
}