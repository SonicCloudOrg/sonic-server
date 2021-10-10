package com.sonic.task.feign.fallback;

import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.task.feign.FolderFeignClient;
import org.springframework.stereotype.Component;

@Component
public class FolderFeignClientFallBack implements FolderFeignClient {

    @Override
    public RespModel delete(int day) {
        return new RespModel(RespEnum.SERVICE_NOT_FOUND);
    }
}