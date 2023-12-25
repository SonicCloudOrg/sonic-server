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
package org.cloud.sonic.controller.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.mapper.DevicesMapper;
import org.cloud.sonic.controller.mapper.TestSuitesDevicesMapper;
import org.cloud.sonic.controller.models.domain.Agents;
import org.cloud.sonic.controller.models.domain.Devices;
import org.cloud.sonic.controller.models.domain.TestSuitesDevices;
import org.cloud.sonic.controller.models.domain.Users;
import org.cloud.sonic.controller.models.http.DeviceDetailChange;
import org.cloud.sonic.controller.models.http.OccupyParams;
import org.cloud.sonic.controller.models.http.UpdateDeviceImg;
import org.cloud.sonic.controller.models.interfaces.DeviceStatus;
import org.cloud.sonic.controller.models.interfaces.PlatformType;
import org.cloud.sonic.controller.services.AgentsService;
import org.cloud.sonic.controller.services.DevicesService;
import org.cloud.sonic.controller.services.UsersService;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.cloud.sonic.controller.transport.TransportWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;

import static org.cloud.sonic.common.http.RespEnum.DELETE_OK;

/**
 * @author ZhouYiXun
 * @des 设备逻辑层实现
 * @date 2021/8/16 22:51
 */
@Service
@Slf4j
public class DevicesServiceImpl extends SonicServiceImpl<DevicesMapper, Devices> implements DevicesService {

    @Autowired
    private DevicesMapper devicesMapper;
    @Autowired
    private UsersService usersService;
    @Autowired
    private TestSuitesDevicesMapper testSuitesDevicesMapper;
    @Autowired
    private AgentsService agentsService;

    @Override
    public RespModel occupy(OccupyParams occupyParams, String token) {
        Devices devices = findByUdId(occupyParams.getUdId());
        if (devices != null) {
            if (devices.getStatus().equals(DeviceStatus.ONLINE)) {
                Agents agents = agentsService.findById(devices.getAgentId());
                if (agents != null) {
                    JSONObject jsonObject = (JSONObject) JSONObject.toJSON(occupyParams);
                    jsonObject.put("msg", "occupy");
                    jsonObject.put("token", token);
                    jsonObject.put("platform", devices.getPlatform());
                    TransportWorker.send(agents.getId(), jsonObject);
                    JSONObject result = new JSONObject();
                    switch (devices.getPlatform()) {
                        case PlatformType.ANDROID -> {
                            if (occupyParams.getSasRemotePort() != 0) {
                                result.put("sas", String.format("adb connect %s:%d", agents.getHost(), occupyParams.getSasRemotePort()));
                            }
                            if (occupyParams.getUia2RemotePort() != 0) {
                                result.put("uia2", String.format("http://%s:%d/uia/%d", agents.getHost(), agents.getPort(), occupyParams.getUia2RemotePort()));
                            }
                        }
                        case PlatformType.IOS -> {
                            if (occupyParams.getSibRemotePort() != 0) {
                                result.put("sib", String.format("sib remote connect --host %s -p %d", agents.getHost(), occupyParams.getSibRemotePort()));
                            }
                            if (occupyParams.getWdaServerRemotePort() != 0) {
                                result.put("wdaServer", String.format("http://%s:%d", agents.getHost(), occupyParams.getWdaServerRemotePort()));
                            }
                            if (occupyParams.getWdaMjpegRemotePort() != 0) {
                                result.put("wdaMjpeg", String.format("http://%s:%d", agents.getHost(), occupyParams.getWdaMjpegRemotePort()));
                            }
                        }
                    }
                    return new RespModel<>(RespEnum.HANDLE_OK, result);
                } else {
                    return new RespModel<>(RespEnum.ID_NOT_FOUND);
                }
            } else {
                return new RespModel<>(RespEnum.DEVICE_NOT_FOUND);
            }
        } else {
            return new RespModel<>(RespEnum.DEVICE_NOT_FOUND);
        }
    }

    @Override
    public RespModel release(String udId, String token) {
        Users users = usersService.getUserInfo(token);
        Devices devices = findByUdId(udId);
        if (devices != null) {
            if (!devices.getUser().equals(users.getUserName())) {
                return new RespModel<>(RespEnum.UNAUTHORIZED);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", "release");
            jsonObject.put("udId", udId);
            jsonObject.put("platform", devices.getPlatform());
            TransportWorker.send(devices.getAgentId(), jsonObject);
            return new RespModel<>(RespEnum.HANDLE_OK);
        } else {
            return new RespModel<>(RespEnum.DEVICE_NOT_FOUND);
        }
    }

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
    public void updatePosition(int id, int position) {
        Devices o = lambdaQuery().eq(Devices::getId, id).one();
        Devices devices = lambdaQuery().eq(Devices::getAgentId, o.getAgentId()).eq(Devices::getPosition, position).one();
        if (devices != null) {
            devices.setPosition(0);
            save(devices);
        }
        o.setPosition(position);
        save(o);
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
    public Page<Devices> findAll(List<String> iOSVersion, List<String> androidVersion, List<String> hmVersion, List<String> manufacturer,
                                 List<String> cpu, List<String> size, List<Integer> agentId, List<String> status,
                                 String deviceInfo, Page<Devices> pageable) {
        LambdaQueryChainWrapper<Devices> chainWrapper = new LambdaQueryChainWrapper<>(devicesMapper);
        if (androidVersion != null || iOSVersion != null || hmVersion != null) {
            chainWrapper.and(i -> {
                if (androidVersion != null) {
                    i.or().eq(Devices::getPlatform, PlatformType.ANDROID).eq(Devices::getIsHm, 0)
                            .and(j -> {
                                for (String v : androidVersion) {
                                    j.or().likeRight(Devices::getVersion, v);
                                }
                            });
                }
                if (iOSVersion != null) {
                    i.or().eq(Devices::getPlatform, PlatformType.IOS).and(j -> {
                        for (String v : iOSVersion) {
                            j.or().likeRight(Devices::getVersion, v);
                        }
                    });
                }
                if (hmVersion != null) {
                    i.or().eq(Devices::getPlatform, PlatformType.ANDROID).eq(Devices::getIsHm, 1)
                            .and(j -> {
                                for (String v : hmVersion) {
                                    j.or().likeRight(Devices::getVersion, v);
                                }
                            });
                }
            });
        }

        if (manufacturer != null && manufacturer.size() > 0) {
            chainWrapper.in(Devices::getManufacturer, manufacturer);
        }

        if (cpu != null && cpu.size() > 0) {
            chainWrapper.in(Devices::getCpu, cpu);
        }

        if (size != null && size.size() > 0) {
            chainWrapper.in(Devices::getSize, size);
        }

        if (agentId != null && agentId.size() > 0) {
            chainWrapper.in(Devices::getAgentId, agentId);
        }

        if (status != null && status.size() > 0) {
            chainWrapper.in(Devices::getStatus, status);
        }

        if (StringUtils.hasText(deviceInfo)) {
        	chainWrapper.and(q -> {
        		q.like(Devices::getUdId, deviceInfo).or().like(Devices::getModel, deviceInfo).or().like(Devices::getNickName, deviceInfo).or().like(Devices::getChiName, deviceInfo);
        	});
        }

        chainWrapper.last("order by case\n" +
                "        when status='ONLINE' then 1\n" +
                "        when status='DEBUGGING' then 2\n" +
                "        when status='TESTING' then 3\n" +
                "        when status='ERROR' then 4\n" +
                "        when status='UNAUTHORIZED' then 5\n" +
                "        when status='OFFLINE' then 6\n" +
                "        when status='DISCONNECTED' then 7 else 8 end asc,\n" +
                "        status asc,\n" +
                "        id desc");

        return chainWrapper.page(pageable);
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
        List<Devices> devicesList = lambdaQuery().eq(Devices::getAgentId, agentId).eq(Devices::getUdId, udId).list();
        if (devicesList.size() > 0) {
            return devicesList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Devices findByUdId(String udId) {
        List<Devices> devicesList = lambdaQuery().eq(Devices::getUdId, udId).list();
        if (devicesList.size() > 0) {
            return devicesList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public JSONObject getFilterOption() {
        JSONObject jsonObject = new JSONObject();
        List<String> cpuList = devicesMapper.findCpuList();
        if (cpuList.contains("unknown")) {
            cpuList.remove("unknown");
            cpuList.add("unknown");
        }
        cpuList.remove("");
        jsonObject.put("cpu", cpuList);
        List<String> sizeList = devicesMapper.findSizeList();
        if (sizeList.contains("unknown")) {
            sizeList.remove("unknown");
            sizeList.add("unknown");
        }
        sizeList.remove("");
        jsonObject.put("size", sizeList);
        return jsonObject;
    }

    @Override
    public void deviceStatus(JSONObject jsonMsg) {
        Devices devices = findByUdId(jsonMsg.getString("udId"));
        if (devices == null) {
            devices = new Devices();
            devices.setUdId(jsonMsg.getString("udId"));
            devices.setNickName("");
            devices.setUser("");
            devices.setPassword("");
            devices.setImgUrl("");
            devices.setPosition(0);
            devices.setTemperature(0);
            devices.setVoltage(0);
            devices.setLevel(0);
            devices.setIsHm(0);
        }
        devices.setAgentId(jsonMsg.getInteger("agentId"));
        if (jsonMsg.getString("name") != null) {
            if (!jsonMsg.getString("name").equals("unknown")) {
                devices.setName(jsonMsg.getString("name"));
            }
        }
        if (jsonMsg.getString("model") != null) {
            if (!jsonMsg.getString("model").equals("unknown")) {
                devices.setModel(jsonMsg.getString("model"));
                devices.setChiName(getName(jsonMsg.getString("model")));
            }
        }
        if (jsonMsg.getString("version") != null) {
            devices.setVersion(jsonMsg.getString("version"));
        }
        if (jsonMsg.getInteger("platform") != null) {
            devices.setPlatform(jsonMsg.getInteger("platform"));
        }
        if (jsonMsg.getInteger("isHm") != null) {
            devices.setIsHm(jsonMsg.getInteger("isHm"));
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
                devices.setVoltage(d.getInteger("vol"));
                save(devices);
            }
        }
    }

    @Override
    public Integer findTemper() {
        OptionalDouble tempers = new LambdaQueryChainWrapper<>(devicesMapper).ne(Devices::getTemperature, 0)
                .in(Devices::getStatus, Arrays.asList(DeviceStatus.ONLINE, DeviceStatus.DEBUGGING, DeviceStatus.TESTING))
                .list().stream().mapToInt(Devices::getTemperature).average();
        return tempers.isPresent() ? (int) tempers.getAsDouble() : 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RespModel<String> delete(int id) {
        Devices devices = devicesMapper.selectById(id);
        if (ObjectUtils.isEmpty(devices)) {
            return new RespModel<>(RespEnum.DEVICE_NOT_FOUND);
        }
        if (devices.getStatus().equals(DeviceStatus.OFFLINE) || devices.getStatus().equals(DeviceStatus.DISCONNECTED)) {
            devicesMapper.deleteById(id);
            testSuitesDevicesMapper.delete(
                    new LambdaQueryWrapper<TestSuitesDevices>().eq(TestSuitesDevices::getDevicesId, id)
            );
        } else {
            return new RespModel<>(3005, "device.not.offline");
        }
        return new RespModel<>(DELETE_OK);
    }

}
