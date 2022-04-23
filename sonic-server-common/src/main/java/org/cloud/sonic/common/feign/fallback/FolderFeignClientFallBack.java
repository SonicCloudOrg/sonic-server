package org.cloud.sonic.common.feign.fallback;

import org.cloud.sonic.common.feign.FolderFeignClient;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FolderFeignClientFallBack implements FolderFeignClient {

    @Override
    public RespModel<String> delete(int day) {
        return new RespModel<>(RespEnum.SERVICE_NOT_FOUND);
    }

    @Override
    public RespModel<String> uploadFiles(MultipartFile file, String type) {
        return new RespModel<>(RespEnum.SERVICE_NOT_FOUND);
    }

    @Override
    public RespModel<String> uploadRecord(MultipartFile file, String uuid, int index, int total) {
        return new RespModel<>(RespEnum.SERVICE_NOT_FOUND);
    }
}