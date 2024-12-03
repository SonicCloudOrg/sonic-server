package org.cloud.sonic.feign;

import org.cloud.sonic.http.RespModel;
import org.cloud.sonic.feign.fallback.FolderFeignClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "sonic-server-folder", fallback = FolderFeignClientFallBack.class)
public interface FolderFeignClient {

    @DeleteMapping("/files")
    RespModel delete(@RequestParam(name = "day") int day);
}
