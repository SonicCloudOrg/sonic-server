package org.cloud.sonic.controller.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.common.services.TestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenwenjie.star
 * @date 2022/4/7 11:27 下午
 */
@Api(tags = "RPC测试接口")
@RestController
@RequestMapping("/rpcTest")
public class RPCTestController {

    @DubboReference
    private TestService testService;

    @WebAspect
    @ApiOperation(value = "rpc测试用", notes = "rpc测试用")
    @RequestMapping
    public RespModel<String> rpcTest() {
        return new RespModel<>(RespEnum.HANDLE_OK, testService.test());
    }

}
