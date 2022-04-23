package org.cloud.sonic.task.feign;

import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.task.feign.fallback.FolderFeignClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "sonic-server-folder", fallback = FolderFeignClientFallBack.class)
public interface FolderFeignClient {
    @GetMapping("/files/delete")
    RespModel delete(@RequestParam(name = "day") int day);
}
