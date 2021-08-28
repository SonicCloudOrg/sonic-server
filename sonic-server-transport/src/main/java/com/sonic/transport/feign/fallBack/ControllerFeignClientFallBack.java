package com.sonic.transport.feign.fallBack;

import com.alibaba.fastjson.JSONObject;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.transport.feign.ControllerFeignClient;
import org.springframework.stereotype.Component;

@Component
public class ControllerFeignClientFallBack implements ControllerFeignClient {
    @Override
    public RespModel deviceStatus(JSONObject jsonObject) {
        return new RespModel(RespEnum.SERVICE_NOT_FOUND);
    }

    @Override
    public RespModel saveAgent(JSONObject agents) {
        return new RespModel(RespEnum.SERVICE_NOT_FOUND);
    }

    @Override
    public RespModel offLine(int id) {
        return new RespModel(RespEnum.SERVICE_NOT_FOUND);
    }
}