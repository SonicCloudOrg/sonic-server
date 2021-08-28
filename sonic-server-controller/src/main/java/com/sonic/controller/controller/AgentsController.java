package com.sonic.controller.controller;

import com.alibaba.fastjson.JSONObject;
import com.sonic.common.config.WebAspect;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.controller.services.AgentsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ZhouYiXun
 * @des
 * @date 2021/8/28 21:49
 */
@Api(tags = "Agent端相关")
@RestController
@RequestMapping("/agents")
public class AgentsController {
    @Autowired
    private AgentsService agentsService;

    @WebAspect
    @PutMapping
    public RespModel save(@RequestBody JSONObject jsonObject) {
        agentsService.save(jsonObject);
        return new RespModel(RespEnum.HANDLE_OK);
    }

    @WebAspect
    @GetMapping("/offLine")
    public RespModel offLine(@RequestParam(name = "id") int id) {
        agentsService.offLine(id);
        return new RespModel(RespEnum.HANDLE_OK);
    }
}
