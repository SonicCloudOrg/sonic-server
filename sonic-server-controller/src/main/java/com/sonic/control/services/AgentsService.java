package com.sonic.control.services;

import com.alibaba.fastjson.JSONObject;
import com.sonic.control.models.Agents;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des Agent逻辑层
 * @date 2021/8/19 22:51
 */
public interface AgentsService {
    List<Agents> findAgents();

    Agents findByIp(String ip);

    void save(Agents agents);

    Agents findTopByName(String name);

    boolean statusChange(int id);
}