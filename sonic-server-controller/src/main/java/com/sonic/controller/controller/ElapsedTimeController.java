package com.sonic.controller.controller;

import com.alibaba.fastjson.JSONObject;
import com.sonic.common.config.WebAspect;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.controller.services.ElapsedTimeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhouYiXun
 * @des
 * @date 2021/8/29 23:11
 */
@Api(tags = "运行时长相关")
@RestController
@RequestMapping("/elapsedTime")
public class ElapsedTimeController {
    @Autowired
    private ElapsedTimeService elapsedTimeService;

    @WebAspect
    @PutMapping
    public RespModel saveElapsedTime(@RequestBody JSONObject jsonObject) {
        elapsedTimeService.save(jsonObject);
        return new RespModel(RespEnum.UPDATE_OK);
    }
}
