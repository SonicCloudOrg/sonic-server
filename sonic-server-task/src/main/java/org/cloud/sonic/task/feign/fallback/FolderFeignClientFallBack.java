package org.cloud.sonic.task.feign.fallback;

import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.task.feign.FolderFeignClient;
import org.springframework.stereotype.Component;

@Component
public class FolderFeignClientFallBack implements FolderFeignClient {

    @Override
    public RespModel delete(int day) {
        return new RespModel(RespEnum.SERVICE_NOT_FOUND);
    }
}