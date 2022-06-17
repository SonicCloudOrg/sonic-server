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

    // todo 删除

    boolean offLine(int id);

    void offLine(Agents agents);

    int auth(String key);

    String findKeyById(int id);

    Agents findById(int id);

    void saveAgents(JSONObject jsonObject);

    void saveAgents(Agents agents);

    /**
     * 会根据 {@link Agents#getLockVersion()} 更新Agent状态
     *
     * @param agents agent对象
     * @return 是否更新成功
     */
    boolean updateAgentsByLockVersion(Agents agents);

    Agents findBySecretKey(String secretKey);
}