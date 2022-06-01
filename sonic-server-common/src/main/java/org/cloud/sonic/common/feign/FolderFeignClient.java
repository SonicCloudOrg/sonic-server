package org.cloud.sonic.common.feign;

import org.cloud.sonic.common.feign.fallback.FolderFeignClientFallBack;
import org.cloud.sonic.common.http.RespModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@FeignClient(value = "sonic-server-folder", fallback = FolderFeignClientFallBack.class)
public interface FolderFeignClient {

    @DeleteMapping("/files")
    RespModel<String> delete(@RequestParam(name = "day") int day);


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    RespModel<String> uploadFiles(@RequestPart(name = "file") MultipartFile file,
                                         @RequestParam(name = "type") String type) throws IOException;

    @PostMapping(value = "/upload/recordFiles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    RespModel<String> uploadRecord(@RequestPart(name = "file") MultipartFile file,
                                          @RequestParam(name = "uuid") String uuid,
                                          @RequestParam(name = "index") int index,
                                          @RequestParam(name = "total") int total) throws IOException;
}
