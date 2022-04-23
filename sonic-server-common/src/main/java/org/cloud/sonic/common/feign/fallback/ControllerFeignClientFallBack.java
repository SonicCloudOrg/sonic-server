package org.cloud.sonic.common.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.common.feign.ControllerFeignClient;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.common.models.domain.Devices;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ControllerFeignClientFallBack implements ControllerFeignClient {
    @Override
    public RespModel deviceStatus(JSONObject jsonObject) {
        return new RespModel(RespEnum.SERVICE_NOT_FOUND);
    }

    @Override
    public RespModel updateUser(JSONObject jsonObject) {
        return new RespModel(RespEnum.SERVICE_NOT_FOUND);
    }

    @Override
    public RespModel refreshDevicesBattery(JSONObject jsonObject) {
        return new RespModel(RespEnum.SERVICE_NOT_FOUND);
    }

    @Override
    public RespModel saveAgent(JSONObject agents) {
        return new RespModel(RespEnum.SERVICE_NOT_FOUND);
    }

    @Override
    public RespModel findAgentById(int id) {
       return new RespModel(RespEnum.SERVICE_NOT_FOUND);
    }

    @Override
    public RespModel offLine(int id) {
        return new RespModel(RespEnum.SERVICE_NOT_FOUND);
    }

    @Override
    public RespModel saveResultDetail(JSONObject jsonObject) {
        return new RespModel(RespEnum.SERVICE_NOT_FOUND);
    }

    @Override
    public RespModel saveElapsed(JSONObject jsonObject) {
        return new RespModel(RespEnum.SERVICE_NOT_FOUND);
    }

    @Override
    public RespModel findDeviceById(int id) {
        return new RespModel(RespEnum.SERVICE_NOT_FOUND);
    }

    @Override
    public RespModel<List<Devices>> listAll(int platform) {
        return new RespModel(RespEnum.SERVICE_NOT_FOUND);
    }

    @Override
    public RespModel findKeyById(int id) {
        return new RespModel(RespEnum.SERVICE_NOT_FOUND);
    }

    @Override
    public RespModel findSteps(int id) {
       return new RespModel(RespEnum.SERVICE_NOT_FOUND);
    }
}