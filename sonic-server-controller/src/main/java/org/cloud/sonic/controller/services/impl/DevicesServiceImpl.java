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
package org.cloud.sonic.controller.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboService;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.mapper.DevicesMapper;
import org.cloud.sonic.controller.mapper.TestSuitesDevicesMapper;
import org.cloud.sonic.common.models.domain.Devices;
import org.cloud.sonic.common.models.domain.TestSuitesDevices;
import org.cloud.sonic.common.models.domain.Users;
import org.cloud.sonic.common.models.http.DeviceDetailChange;
import org.cloud.sonic.common.models.http.UpdateDeviceImg;
import org.cloud.sonic.common.models.interfaces.DeviceStatus;
import org.cloud.sonic.common.models.params.DevicesSearchParams;
import org.cloud.sonic.common.services.DevicesService;
import org.cloud.sonic.common.services.UsersService;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.cloud.sonic.common.http.RespEnum.DELETE_OK;

/**
 * @author ZhouYiXun
 * @des 设备逻辑层实现
 * @date 2021/8/16 22:51
 */
@Service
@DubboService
public class DevicesServiceImpl extends SonicServiceImpl<DevicesMapper, Devices> implements DevicesService {

    @Autowired
    private DevicesMapper devicesMapper;

    @Autowired
    private UsersService usersService;
    @Autowired
    private TestSuitesDevicesMapper testSuitesDevicesMapper;

    @Override
    public boolean saveDetail(DeviceDetailChange deviceDetailChange) {
        if (existsById(deviceDetailChange.getId())) {
            Devices devices = findById(deviceDetailChange.getId());
            devices.setNickName(deviceDetailChange.getNickName());
            devices.setPassword(deviceDetailChange.getPassword());
            save(devices);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void updateDevicesUser(JSONObject jsonObject) {
        Users users = usersService.getUserInfo(jsonObject.getString("token"));
        Devices devices = findByAgentIdAndUdId(jsonObject.getInteger("agentId"),
                jsonObject.getString("udId"));
        devices.setUser(users.getUserName());
        save(devices);
    }

    @Override
    public void updateImg(UpdateDeviceImg updateDeviceImg) {
        if (existsById(updateDeviceImg.getId())) {
            Devices devices = findById(updateDeviceImg.getId());
            devices.setImgUrl(updateDeviceImg.getImgUrl());
            save(devices);
        }
    }

    @Override
    public Page<Devices> findAll(List<String> iOSVersion, List<String> androidVersion, List<String> manufacturer,
                                 List<String> cpu, List<String> size, List<Integer> agentId, List<String> status,
                                 String deviceInfo, Page<Devices> pageable) {
        DevicesSearchParams params = new DevicesSearchParams()
                .setIOSVersion(iOSVersion)
                .setAndroidVersion(androidVersion)
                .setManufacturer(manufacturer)
                .setCpu(cpu)
                .setSize(size)
                .setAgentId(agentId)
                .setStatus(status)
                .setDeviceInfo(deviceInfo);
        return devicesMapper.findByParams(pageable, params);
    }

    @Override
    public List<Devices> findAll(int platform) {
        return lambdaQuery().eq(Devices::getPlatform, platform).orderByDesc(Devices::getId).list();
    }

    @Override
    public List<Devices> findByIdIn(List<Integer> ids) {

        // 不用in查询，以防出现传过来的ids顺序是乱的
        List<Devices> devices = new ArrayList<>();
        for (Integer id : ids) {
            Devices device = findById(id);
            if (ObjectUtils.isEmpty(device)) {
                devices.add(Devices.newDeletedDevice(id));
            } else {
                devices.add(device);
            }
        }

        return devices;
    }

    @Override
    public Devices findByAgentIdAndUdId(int agentId, String udId) {
        return lambdaQuery().eq(Devices::getAgentId, agentId).eq(Devices::getUdId, udId).one();
    }

    @Override
    public JSONObject getFilterOption() {
        JSONObject jsonObject = new JSONObject();
        List<String> cpuList = devicesMapper.findCpuList();
        if (cpuList.contains("未知")) {
            cpuList.remove("未知");
            cpuList.add("未知");
        }
        jsonObject.put("cpu", cpuList);
        List<String> sizeList = devicesMapper.findSizeList();
        if (sizeList.contains("未知")) {
            sizeList.remove("未知");
            sizeList.add("未知");
        }
        jsonObject.put("size", sizeList);
        return jsonObject;
    }

    @Override
    public void deviceStatus(JSONObject jsonMsg) {
        Devices devices = findByAgentIdAndUdId(jsonMsg.getInteger("agentId")
                , jsonMsg.getString("udId"));
        if (devices == null) {
            Devices newDevices = new Devices();
            newDevices.setUdId(jsonMsg.getString("udId"));
            if (jsonMsg.getString("name") != null) {
                newDevices.setName(jsonMsg.getString("name"));
            }
            if (jsonMsg.getString("model") != null) {
                newDevices.setName(jsonMsg.getString("model"));
                newDevices.setChiName(getName(jsonMsg.getString("model")));
            }
            newDevices.setNickName("");
            newDevices.setUser("");
            newDevices.setPlatform(jsonMsg.getInteger("platform"));
            newDevices.setVersion(jsonMsg.getString("version"));
            newDevices.setCpu(jsonMsg.getString("cpu"));
            newDevices.setSize(jsonMsg.getString("size"));
            newDevices.setManufacturer(jsonMsg.getString("manufacturer"));
            newDevices.setAgentId(jsonMsg.getInteger("agentId"));
            newDevices.setStatus(jsonMsg.getString("status"));
            newDevices.setPassword("");
            newDevices.setImgUrl("");
            newDevices.setTemperature(0);
            newDevices.setLevel(0);
            newDevices.setHubNum(0);
            save(newDevices);
        } else {
            devices.setAgentId(jsonMsg.getInteger("agentId"));
            if (jsonMsg.getString("name") != null) {
                if (!jsonMsg.getString("name").equals("未知")) {
                    devices.setName(jsonMsg.getString("name"));
                }
            }
            if (jsonMsg.getString("model") != null) {
                if (!jsonMsg.getString("model").equals("未知")) {
                    devices.setModel(jsonMsg.getString("model"));
                    devices.setChiName(getName(jsonMsg.getString("model")));
                }
            }
            if (jsonMsg.getString("version") != null) {
                devices.setVersion(jsonMsg.getString("version"));
            }
            if (jsonMsg.getString("platform") != null) {
                devices.setPlatform(jsonMsg.getInteger("platform"));
            }
            if (jsonMsg.getString("cpu") != null) {
                devices.setCpu(jsonMsg.getString("cpu"));
            }
            if (jsonMsg.getString("size") != null) {
                devices.setSize(jsonMsg.getString("size"));
            }
            if (jsonMsg.getString("manufacturer") != null) {
                devices.setManufacturer(jsonMsg.getString("manufacturer"));
            }
            if (jsonMsg.getString("status") != null) {
                devices.setStatus(jsonMsg.getString("status"));
            }
            save(devices);
        }
    }

    @Override
    public Devices findById(int id) {
        return baseMapper.selectById(id);
    }

    @Override
    public List<Devices> listByAgentId(int agentId) {
        return lambdaQuery().eq(Devices::getAgentId, agentId).list();
    }

    @Override
    public String getName(String model) {
        InputStream config = getClass().getResourceAsStream("/result.json");
        JSONObject jsonObject = null;
        try {
            jsonObject = JSON.parseObject(config, JSONObject.class);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                config.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonObject.getString(model) == null ? "" : jsonObject.getString(model);
    }

    @Override
    public void refreshDevicesBattery(JSONObject jsonObject) {
        int agentId = jsonObject.getInteger("agentId");
        List<JSONObject> deviceTemList = jsonObject.getJSONArray("detail").toJavaList(JSONObject.class);
        for (JSONObject d : deviceTemList) {
            Devices devices = findByAgentIdAndUdId(agentId, d.getString("udId"));
            if (devices != null) {
                devices.setTemperature(d.getInteger("tem"));
                devices.setLevel(d.getInteger("level"));
                save(devices);
            }
        }
    }

    @Override
    public Integer findTemper() {
        return devicesMapper.findTemper(Arrays.asList(DeviceStatus.ONLINE
                , DeviceStatus.DEBUGGING, DeviceStatus.TESTING));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RespModel<String> delete(int id) {
        Devices devices = devicesMapper.selectById(id);
        if (ObjectUtils.isEmpty(devices)) {
            return new RespModel<>(3004, "设备已被删除过");
        }
        if (devices.getStatus().equals(DeviceStatus.OFFLINE) || devices.getStatus().equals(DeviceStatus.DISCONNECTED)) {
            devicesMapper.deleteById(id);
            testSuitesDevicesMapper.delete(
                    new LambdaQueryWrapper<TestSuitesDevices>().eq(TestSuitesDevices::getDevicesId, id)
            );
        } else {
            return new RespModel<>(3005, "设备不处于离线状态");
        }
        return new RespModel<>(DELETE_OK);
    }

}
