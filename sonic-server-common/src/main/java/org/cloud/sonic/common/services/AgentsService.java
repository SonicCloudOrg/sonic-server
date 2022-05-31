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
package org.cloud.sonic.common.services;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.common.models.domain.Agents;

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
    @Deprecated
    boolean offLine(int id);

    /**
     * 下线agent的所有设备，并且agent也下线
     * 注意：依赖乐观锁，防止zk下线通知跟网络请求乱序导致的状态错乱
     *
     * @param agents agent对象
     */
    void offLine(Agents agents);

    /**
     * 下线agent的所有设备，并且agent状态设置成agentStatus（禁止设置成在线状态）
     *
     * @param agents        agent对象
     * @param agentStatus   agent状态 {@link org.cloud.sonic.common.models.interfaces.AgentStatus}
     */
    void offLine(Agents agents, int agentStatus);

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

    /**
     * 校准agent在线状态，只应该在server端使用
     */
    void correctionStatus();

    boolean checkOnline(Agents agents);

    Agents findByCabinetIdAndStorey(int id, int storey);

    List<JSONObject> findByCabinetForDetail(int id);

    List<Agents> findByCabinetId(int id);
}