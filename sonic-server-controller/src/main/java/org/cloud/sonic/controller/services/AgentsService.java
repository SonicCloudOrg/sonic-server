package org.cloud.sonic.controller.services;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.controller.models.domain.Agents;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des Agent逻辑层
 * @date 2021/8/19 22:51
 */
public interface AgentsService extends IService<Agents> {
    List<Agents> findAgents();

    void updateName(int id, String name);

    boolean offLine(int id);

    int auth(String key);

    String findKeyById(int id);

    Agents findById(int id);

    public void saveAgents(JSONObject jsonObject);

    public void saveAgents(Agents agents);

    public Agents findBySecretKey(String secretKey);
}