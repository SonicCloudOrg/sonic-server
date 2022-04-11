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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.common.models.domain.Devices;
import org.cloud.sonic.common.models.http.DeviceDetailChange;
import org.cloud.sonic.common.models.http.UpdateDeviceImg;

import java.io.IOException;
import java.util.List;

/**
 * @author ZhouYiXun
 * @des 设备逻辑层
 * @date 2021/8/16 22:51
 */
public interface DevicesService extends IService<Devices> {
    boolean saveDetail(DeviceDetailChange deviceDetailChange);

    void updateDevicesUser(JSONObject jsonObject);

    void updateImg(UpdateDeviceImg updateDeviceImg);

    Page<Devices> findAll(List<String> iOSVersion, List<String> androidVersion, List<String> manufacturer,
                          List<String> cpu, List<String> size, List<Integer> agentId, List<String> status,
                          String deviceInfo, Page<Devices> pageable);

    List<Devices> findAll(int platform);

    List<Devices> findByIdIn(List<Integer> ids);

    Devices findByAgentIdAndUdId(int agentId, String udId);


    JSONObject getFilterOption();

    void deviceStatus(JSONObject jsonObject);

    Devices findById(int id);

    List<Devices> listByAgentId(int agentId);

    String getName(String model) throws IOException;

    void refreshDevicesBattery(JSONObject jsonObject);

    Integer findTemper();

    RespModel<String> delete(int id);

    /**
     * 校准所有设备的离线状态（如果设备不在）
     */
    void correctionAllDevicesStatus();
}
