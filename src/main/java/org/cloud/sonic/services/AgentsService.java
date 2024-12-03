/*
 *   sonic-server  Sonic Cloud Real Machine Platform.
 *   Copyright (C) 2022 SonicCloudOrg
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
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

    void update(int id, String name, int highTemp, int highTempTime, int robotType, String robotToken, String robotSecret, int[] alertRobotIds);

    boolean offLine(int id);

    Agents auth(String key);

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

    void errCall(int id, String udId, int tem, int type);
}