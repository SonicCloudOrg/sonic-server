package com.sonic.controller.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.sonic.controller.dao.DevicesRepository;
import com.sonic.controller.models.Devices;
import com.sonic.controller.models.http.DevicePwdChange;
import com.sonic.controller.models.interfaces.DeviceStatus;
import com.sonic.controller.models.interfaces.PlatformType;
import com.sonic.controller.services.DevicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhouYiXun
 * @des 设备逻辑层实现
 * @date 2021/8/16 22:51
 */
@Service
public class DevicesServiceImpl implements DevicesService {
    @Autowired
    private DevicesRepository devicesRepository;

    @Override
    public boolean savePwd(DevicePwdChange devicePwdChange) {
        if (devicesRepository.existsById(devicePwdChange.getId())) {
            Devices devices = devicesRepository.findById(devicePwdChange.getId()).get();
            devices.setPassword(devicePwdChange.getPassword());
            devicesRepository.save(devices);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void save(Devices devices) {
        devicesRepository.save(devices);
    }

    @Override
    public Page<Devices> findAll(List<String> iOSVersion, List<String> androidVersion,
                                 List<String> manufacturer, List<String> cpu, List<String> size,
                                 List<Integer> agentId, List<String> status, String deviceInfo,
                                 Pageable pageable) {
        Specification<Devices> spc = (root, query, cb) -> {
            //根据status的字符串自定义排序
            List<Order> orders = new ArrayList<>();
            orders.add(cb.asc(cb.selectCase()
                    .when(cb.equal(root.get("status").as(String.class), DeviceStatus.ONLINE), 1)
                    .when(cb.equal(root.get("status").as(String.class), DeviceStatus.DEBUGGING), 2)
                    .when(cb.equal(root.get("status").as(String.class), DeviceStatus.TESTING), 3)
                    .when(cb.equal(root.get("status").as(String.class), DeviceStatus.ERROR), 4)
                    .when(cb.equal(root.get("status").as(String.class), DeviceStatus.UNAUTHORIZED), 5)
                    .when(cb.equal(root.get("status").as(String.class), DeviceStatus.OFFLINE), 6)
                    .when(cb.equal(root.get("status").as(String.class), DeviceStatus.DISCONNECTED), 7)
                    .otherwise(8)));
            orders.add(cb.asc(root.get("status")));
            orders.add(cb.desc(root.get("id")));
            query.orderBy(orders);
            List<Predicate> predicateList = new ArrayList<>();
            if (androidVersion != null || iOSVersion != null) {
                List<Predicate> androidAndiOS = new ArrayList<>();
                //查找类型为安卓的并且version%的设备
                if (androidVersion != null) {
                    List<Predicate> list = new ArrayList<>();
                    for (String version : androidVersion) {
                        List<Predicate> platform = new ArrayList<>();
                        platform.add(cb.equal(root.get("platform"), PlatformType.ANDROID));
                        platform.add(cb.like(root.get("version"), version + "%"));
                        Predicate[] p = new Predicate[platform.size()];
                        list.add(cb.and(platform.toArray(p)));
                    }
                    Predicate[] l = new Predicate[list.size()];
                    androidAndiOS.add(cb.or(list.toArray(l)));
                }
                //查找类型为iOS的并且version%的设备
                if (iOSVersion != null) {
                    List<Predicate> list = new ArrayList<>();
                    for (String version : iOSVersion) {
                        List<Predicate> platform = new ArrayList<>();
                        platform.add(cb.equal(root.get("platform"), PlatformType.IOS));
                        platform.add(cb.like(root.get("version"), version + "%"));
                        Predicate[] p = new Predicate[platform.size()];
                        list.add(cb.and(platform.toArray(p)));
                    }
                    Predicate[] l = new Predicate[list.size()];
                    androidAndiOS.add(cb.or(list.toArray(l)));
                }
                //最后两个条件为or
                Predicate[] result = new Predicate[androidAndiOS.size()];
                predicateList.add(cb.or(androidAndiOS.toArray(result)));
            }
            if (manufacturer != null) {
                CriteriaBuilder.In<Object> in = cb.in(root.get("manufacturer"));
                for (String man : manufacturer) {
                    in.value(man);
                }
                predicateList.add(cb.and(in));
            }
            if (cpu != null) {
                CriteriaBuilder.In<Object> in = cb.in(root.get("cpu"));
                for (String c : cpu) {
                    in.value(c);
                }
                predicateList.add(cb.and(in));
            }
            if (size != null) {
                CriteriaBuilder.In<Object> in = cb.in(root.get("size"));
                for (String s : size) {
                    in.value(s);
                }
                predicateList.add(cb.and(in));
            }
            if (agentId != null) {
                CriteriaBuilder.In<Object> in = cb.in(root.get("agentId"));
                for (int a : agentId) {
                    in.value(a);
                }
                predicateList.add(cb.and(in));
            }
            if (status != null) {
                CriteriaBuilder.In<Object> in = cb.in(root.get("status"));
                for (String s : status) {
                    in.value(s);
                }
                predicateList.add(cb.and(in));
            }
            if (deviceInfo != null) {
                //因为是型号或者udId，所以两个条件为or
                List<Predicate> modelOrUdId = new ArrayList<>();
                modelOrUdId.add(cb.like(root.get("model"), "%" + deviceInfo + "%"));
                modelOrUdId.add(cb.like(root.get("udId"), "%" + deviceInfo + "%"));
                Predicate[] result = new Predicate[modelOrUdId.size()];
                predicateList.add(cb.or(modelOrUdId.toArray(result)));
            }
            if (predicateList.size() != 0) {
                Predicate[] p = new Predicate[predicateList.size()];
                return query.where(predicateList.toArray(p)).getRestriction();
            } else {
                return query.getRestriction();
            }
        };
        return devicesRepository.findAll(spc, pageable);
    }

    @Override
    public List<Devices> findByIdIn(List<Integer> ids) {
        return devicesRepository.findByIdIn(ids);
    }

    @Override
    public Devices findByAgentIdAndUdId(int agentId, String udId) {
        return devicesRepository.findByAgentIdAndUdId(agentId, udId);
    }

    @Override
    public JSONObject getFilterOption() {
        JSONObject jsonObject = new JSONObject();
        List<String> cpuList = devicesRepository.findCpuList();
        if (cpuList.contains("未知")) {
            cpuList.remove("未知");
            cpuList.add("未知");
        }
        jsonObject.put("cpu", cpuList);
        List<String> sizeList = devicesRepository.findSizeList();
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
            newDevices.setPlatform(jsonMsg.getInteger("platform"));
            newDevices.setVersion(jsonMsg.getString("version"));
            newDevices.setCpu(jsonMsg.getString("cpu"));
            newDevices.setSize(jsonMsg.getString("size"));
            newDevices.setManufacturer(jsonMsg.getString("manufacturer"));
            newDevices.setAgentId(jsonMsg.getInteger("agentId"));
            newDevices.setStatus(jsonMsg.getString("status"));
            newDevices.setPassword("");
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
        if (devicesRepository.existsById(id)) {
            return devicesRepository.findById(id).get();
        } else {
            return null;
        }
    }
}
