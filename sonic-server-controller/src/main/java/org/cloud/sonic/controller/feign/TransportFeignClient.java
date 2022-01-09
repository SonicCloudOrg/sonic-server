package org.cloud.sonic.controller.feign;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.feign.fallback.TransportFeignClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author ZhouYiXun
 * @des 通过feign负载均衡调用transport模块接口
 * @date 2021/8/21 16:51
 */
@FeignClient(value = "sonic-server-transport", fallback = TransportFeignClientFallBack.class)
public interface TransportFeignClient {

    @PostMapping("/exchange/sendTestData")
    RespModel sendTestData(@RequestBody JSONObject jsonObject);
}
