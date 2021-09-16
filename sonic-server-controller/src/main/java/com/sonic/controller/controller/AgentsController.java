package com.sonic.controller.controller;

import com.alibaba.fastjson.JSONObject;
import com.sonic.common.config.WebAspect;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.controller.models.Agents;
import com.sonic.controller.services.AgentsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @ApiOperation(value = "查询所有代理端", notes = "获取所有代理端以及详细信息")
    @GetMapping
    public RespModel<List<Agents>> findAgents() {
        return new RespModel(RespEnum.SEARCH_OK, agentsService.findAgents());
    }

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

    @WebAspect
    @GetMapping("/auth")
    public RespModel auth(@RequestParam(name = "key") String key) {
        return new RespModel(RespEnum.SEARCH_OK, agentsService.auth(key));
    }

    @WebAspect
    @GetMapping("/findKeyById")
    public RespModel findKeyById(@RequestParam(name = "id") int id) {
        String key = agentsService.findKeyById(id);
        if (key != null) {
            return new RespModel(RespEnum.SEARCH_OK, key);
        } else {
            return new RespModel(RespEnum.ID_NOT_FOUND);
        }
    }
}
