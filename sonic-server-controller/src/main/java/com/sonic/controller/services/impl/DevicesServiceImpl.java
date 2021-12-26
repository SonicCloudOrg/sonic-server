package com.sonic.controller.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sonic.controller.mapper.DevicesMapper;
import com.sonic.controller.models.domain.Devices;
import com.sonic.controller.models.domain.Users;
import com.sonic.controller.models.http.DeviceDetailChange;
import com.sonic.controller.models.http.UpdateDeviceImg;
import com.sonic.controller.models.interfaces.DeviceStatus;
import com.sonic.controller.models.params.DevicesSearchParams;
import com.sonic.controller.services.DevicesService;
import com.sonic.controller.services.UsersService;
import com.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @author ZhouYiXun
 * @des 设备逻辑层实现
 * @date 2021/8/16 22:51
 */
@Service
public class DevicesServiceImpl extends SonicServiceImpl<DevicesMapper, Devices> implements DevicesService {

    @Autowired
    private DevicesMapper devicesMapper;

    @Autowired
    private UsersService usersService;

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
        return lambdaQuery().in(Devices::getId, ids).list();
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
            devices.setStatus(jsonMsg.getString("status"));
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
    public void refreshDevicesTemper(JSONObject jsonObject) {
        int agentId = jsonObject.getInteger("agentId");
        List<JSONObject> deviceTemList = jsonObject.getJSONArray("detail").toJavaList(JSONObject.class);
        for (JSONObject d : deviceTemList) {
            Devices devices = findByAgentIdAndUdId(agentId, d.getString("udId"));
            if (devices != null) {
                devices.setTemperature(d.getInteger("tem"));
                save(devices);
            }
        }
    }

    @Override
    public Integer findTemper() {
        return devicesMapper.findTemper(Arrays.asList(DeviceStatus.ONLINE
                ,DeviceStatus.DEBUGGING,DeviceStatus.TESTING));
    }

}
