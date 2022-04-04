package org.cloud.sonic.common.feign;

import org.cloud.sonic.common.feign.fallback.FolderFeignClientFallBack;
import org.cloud.sonic.common.http.RespModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "sonic-server-folder", fallback = FolderFeignClientFallBack.class)
public interface FolderFeignClient {

    @GetMapping("/files/delete")
    public RespModel delete(@RequestParam(name = "day") int day);
}
