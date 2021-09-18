package com.sonic.controller.controller;

import com.alibaba.fastjson.JSONObject;
import com.sonic.common.config.WebAspect;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.controller.services.ResultDetailService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhouYiXun
 * @des
 * @date 2021/8/29 16:59
 */
@Api(tags = "测试结果详情相关")
@RestController
@RequestMapping("/resultDetail")
public class ResultDetailController {
    @Autowired
    private ResultDetailService resultDetailService;

    @WebAspect
    @PostMapping
    public RespModel save(@RequestBody JSONObject jsonObject) {
        resultDetailService.saveByTransport(jsonObject);
        return new RespModel(RespEnum.HANDLE_OK);
    }
}
