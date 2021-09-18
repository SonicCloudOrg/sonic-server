package com.sonic.controller.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.sonic.controller.dao.ElapsedTimeRepository;
import com.sonic.controller.models.Devices;
import com.sonic.controller.models.ElapsedTime;
import com.sonic.controller.models.Results;
import com.sonic.controller.services.DevicesService;
import com.sonic.controller.services.ElapsedTimeService;
import com.sonic.controller.services.ResultsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author ZhouYiXun
 * @des
 * @date 2021/8/29 23:02
 */
@Service
public class ElapsedTimeServiceImpl implements ElapsedTimeService {
    @Autowired
    private ResultsService resultsService;
    @Autowired
    private DevicesService devicesService;
    @Autowired
    private ElapsedTimeRepository elapsedTimeRepository;

    @Override
    public void save(JSONObject jsonObject) {
        Results resultsCheck = resultsService.findById(jsonObject.getInteger("rid"));
        if (resultsCheck != null) {
            Devices devices = devicesService.findByAgentIdAndUdId(jsonObject.getInteger("agentId"),
                    jsonObject.getString("udId"));
            if (devices != null) {
                String version;
                if (devices.getVersion().indexOf(".") == -1) {
                    version = devices.getVersion().replace(" ", "");
                } else {
                    version = devices.getVersion().substring(0, devices.getVersion().indexOf("."));
                }
                ElapsedTime has = elapsedTimeRepository.findByProjectIdAndTimeAndPlatformAndPackageVersionAndManufacturerAndSystemVersion(
                        resultsCheck.getProjectId(), jsonObject.getDate("time"), jsonObject.getInteger("pf"),
                        jsonObject.getString("ver"), devices.getManufacturer(), version);
                if (has != null) {
                    has.setRunTime(has.getRunTime() + (int) Math.rint(jsonObject.getInteger("run") / 1000));
                    elapsedTimeRepository.save(has);
                } else {
                    ElapsedTime elapsedTime = new ElapsedTime();
                    elapsedTime.setTime(jsonObject.getDate("time"));
                    elapsedTime.setProjectId(resultsCheck.getProjectId());
                    elapsedTime.setRunTime((int) Math.rint(jsonObject.getInteger("run") / 1000));
                    elapsedTime.setManufacturer(devices.getManufacturer());
                    elapsedTime.setPlatform(jsonObject.getInteger("pf"));
                    elapsedTime.setPackageVersion(jsonObject.getString("ver"));
                    elapsedTime.setSystemVersion(version);
                    elapsedTimeRepository.save(elapsedTime);
                }
            }
        }
    }
}
