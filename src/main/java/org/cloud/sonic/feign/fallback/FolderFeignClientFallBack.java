package org.cloud.sonic.feign.fallback;

import org.cloud.sonic.http.RespEnum;
import org.cloud.sonic.http.RespModel;
import org.cloud.sonic.feign.FolderFeignClient;
import org.springframework.stereotype.Component;

@Component
public class FolderFeignClientFallBack implements FolderFeignClient {

    @Override
    public RespModel delete(int day) {
        return new RespModel(RespEnum.SERVICE_NOT_FOUND);
    }
}