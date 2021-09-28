package com.sonic.controller.controller;

import com.sonic.common.config.WebAspect;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.controller.models.Modules;
import com.sonic.controller.services.ModulesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "模块管理相关")
@RestController
@RequestMapping("/modules")
public class ModulesController {
    @Autowired
    private ModulesService modulesService;

    @WebAspect
    @ApiOperation(value = "更新模块信息", notes = "新增或更新对应的模块信息")
    @PutMapping
    public RespModel save(@Validated @RequestBody Modules modules) {
        modulesService.save(modules);
        return new RespModel(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @ApiOperation(value = "查找模块列表", notes = "查找对应项目id的模块列表")
    @ApiImplicitParam(name = "projectId", value = "项目id", dataTypeClass = Integer.class)
    @GetMapping("/list")
    public RespModel<List<Modules>> findByProjectId(@RequestParam(name = "projectId") int projectId) {
        return new RespModel(RespEnum.SEARCH_OK, modulesService.findByProjectId(projectId));
    }

    @WebAspect
    @ApiOperation(value = "删除模块", notes = "删除对应id的模块")
    @ApiImplicitParam(name = "id", value = "模块id", dataTypeClass = Integer.class)
    @DeleteMapping
    public RespModel delete(@RequestParam(name = "id") int id) {
        if (modulesService.delete(id)) {
            return new RespModel(RespEnum.DELETE_OK);
        } else {
            return new RespModel(RespEnum.DELETE_ERROR);
        }
    }

    @WebAspect
    @ApiOperation(value = "查看模块信息", notes = "查看对应id的模块信息")
    @ApiImplicitParam(name = "id", value = "模块id", dataTypeClass = Integer.class)
    @GetMapping
    public RespModel<Modules> findById(@RequestParam(name = "id") int id) {
        Modules modules = modulesService.findById(id);
        if (modules != null) {
            return new RespModel(RespEnum.SEARCH_OK, modules);
        } else {
            return new RespModel(RespEnum.ID_NOT_FOUND);
        }
    }
}
