package com.sonic.controller.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.controller.feign.TransportFeignClient;
import org.springframework.stereotype.Component;

/**
 * @author ZhouYiXun
 * @des 熔断降级类
 * @date 2021/8/21 16:51
 */
@Component
public class TransportFeignClientFallBack implements TransportFeignClient {

    @Override
    public RespModel sendTestData(JSONObject jsonObject) {
        return new RespModel(RespEnum.SERVICE_NOT_FOUND);
    }
}
