package org.cloud.sonic.controller.feign;

import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.feign.fallback.FolderFeignClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "sonic-server-folder", fallback = FolderFeignClientFallBack.class)
public interface FolderFeignClient {

    @DeleteMapping("/files")
    RespModel delete(@RequestParam(name = "day") int day);
}
