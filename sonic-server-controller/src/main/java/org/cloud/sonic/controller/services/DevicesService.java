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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.models.domain.Devices;
import org.cloud.sonic.controller.models.http.DeviceDetailChange;
import org.cloud.sonic.controller.models.http.OccupyParams;
import org.cloud.sonic.controller.models.http.UpdateDeviceImg;

import java.io.IOException;
import java.util.List;

/**
 * @author ZhouYiXun
 * @des 设备逻辑层
 * @date 2021/8/16 22:51
 */
public interface DevicesService extends IService<Devices> {

    RespModel occupy(OccupyParams occupyParams, String token);

    RespModel release(String udId, String token);

    boolean saveDetail(DeviceDetailChange deviceDetailChange);

    void updatePosition(int id, int position);

    void updateDevicesUser(JSONObject jsonObject);

    void updateImg(UpdateDeviceImg updateDeviceImg);

    Page<Devices> findAll(List<String> iOSVersion, List<String> androidVersion, List<String> hmVersion, List<String> manufacturer,
                          List<String> cpu, List<String> size, List<Integer> agentId, List<String> status,
                          String deviceInfo, Page<Devices> pageable);

    List<Devices> findAll(int platform);

    List<Devices> findByIdIn(List<Integer> ids);

    Devices findByAgentIdAndUdId(int agentId, String udId);

    Devices findByUdId(String udId);

    JSONObject getFilterOption();

    void deviceStatus(JSONObject jsonObject);

    Devices findById(int id);

    List<Devices> listByAgentId(int agentId);

    String getName(String model) throws IOException;

    void refreshDevicesBattery(JSONObject jsonObject);

    Integer findTemper();

    RespModel<String> delete(int id);

}
