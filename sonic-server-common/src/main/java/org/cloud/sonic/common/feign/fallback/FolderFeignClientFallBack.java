package org.cloud.sonic.common.feign.fallback;

import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.springframework.stereotype.Component;
import org.cloud.sonic.common.feign.FolderFeignClient;

@Component
public class FolderFeignClientFallBack implements FolderFeignClient {

    @Override
    public RespModel delete(int day) {
        return new RespModel(RespEnum.SERVICE_NOT_FOUND);
    }
}