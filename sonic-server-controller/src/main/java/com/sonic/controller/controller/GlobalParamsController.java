package com.sonic.controller.controller;

import com.sonic.common.config.WebAspect;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.controller.models.GlobalParams;
import com.sonic.controller.services.GlobalParamsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "公共参数相关")
@RestController
@RequestMapping("/globalParams")
public class GlobalParamsController {
    @Autowired
    private GlobalParamsService globalParamsService;

    @WebAspect
    @ApiOperation(value = "更新公共参数", notes = "新增或更新对应的公共参数")
    @PutMapping
    public RespModel save(@Validated @RequestBody GlobalParams globalParams) {
        globalParamsService.save(globalParams);
        return new RespModel(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @ApiOperation(value = "查找公共参数", notes = "查找对应项目id的公共参数列表")
    @ApiImplicitParam(name = "projectId", value = "项目id", dataTypeClass = Integer.class)
    @GetMapping("/list")
    public RespModel<List<GlobalParams>> findByProjectId(@RequestParam(name = "projectId") int projectId) {
        return new RespModel(RespEnum.SEARCH_OK, globalParamsService.findAll(projectId));
    }

    @WebAspect
    @ApiOperation(value = "删除公共参数", notes = "删除对应id的公共参数")
    @ApiImplicitParam(name = "id", value = "id", dataTypeClass = Integer.class)
    @DeleteMapping
    public RespModel delete(@RequestParam(name = "id") int id) {
        if (globalParamsService.delete(id)) {
            return new RespModel(RespEnum.DELETE_OK);
        } else {
            return new RespModel(RespEnum.DELETE_ERROR);
        }
    }

    @WebAspect
    @ApiOperation(value = "查看公共参数信息", notes = "查看对应id的公共参数")
    @ApiImplicitParam(name = "id", value = "id", dataTypeClass = Integer.class)
    @GetMapping
    public RespModel<GlobalParams> findById(@RequestParam(name = "id") int id) {
        GlobalParams globalParams = globalParamsService.findById(id);
        if (globalParams != null) {
            return new RespModel(RespEnum.SEARCH_OK, globalParams);
        } else {
            return new RespModel(RespEnum.ID_NOT_FOUND);
        }
    }
}
