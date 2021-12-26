package com.sonic.task.feign.fallback;

import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.task.feign.ControllerFeignClient;
import org.springframework.stereotype.Component;

@Component
public class ControllerFeignClientFallBack implements ControllerFeignClient {

    @Override
    public RespModel runSuite(int id) {
        return new RespModel(RespEnum.SERVICE_NOT_FOUND);
    }

    @Override
    public RespModel sendDayReport() {
        return new RespModel(RespEnum.SERVICE_NOT_FOUND);
    }

    @Override
    public RespModel sendWeekReport() {
        return new RespModel(RespEnum.SERVICE_NOT_FOUND);
    }

    @Override
    public RespModel clean(int day) {
        return new RespModel(RespEnum.SERVICE_NOT_FOUND);
    }
}