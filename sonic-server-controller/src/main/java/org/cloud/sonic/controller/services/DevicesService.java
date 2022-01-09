package org.cloud.sonic.controller.services;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.controller.models.domain.Devices;
import org.cloud.sonic.controller.models.http.DeviceDetailChange;
import org.cloud.sonic.controller.models.http.UpdateDeviceImg;

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

    void refreshDevicesTemper(JSONObject jsonObject);

    Integer findTemper();
}
