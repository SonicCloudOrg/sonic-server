package org.cloud.sonic.controller.feign.fallback;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.feign.TransportFeignClient;
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
