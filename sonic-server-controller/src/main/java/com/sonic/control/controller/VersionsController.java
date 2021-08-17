package com.sonic.control.controller;

import com.sonic.common.config.WebAspect;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.control.models.Versions;
import com.sonic.control.services.VersionsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "版本迭代相关")
@RestController
@RequestMapping("/versions")
public class VersionsController {
    @Autowired
    private VersionsService versionsService;

    @WebAspect
    @ApiOperation(value = "更新版本迭代", notes = "新增或更改版本迭代信息")
    @PutMapping
    public RespModel save(@Validated @RequestBody Versions versions) {
        versionsService.save(versions);
        return new RespModel(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @ApiOperation(value = "查询版本迭代列表", notes = "用于查询对应项目id下的版本迭代列表")
    @ApiImplicitParam(name = "projectId", value = "项目id", dataTypeClass = Integer.class)
    @GetMapping("/list")
    public RespModel<List<Versions>> findByProjectId(@RequestParam(name = "projectId") int projectId) {
        return new RespModel(RespEnum.SEARCH_OK, versionsService.findByProjectId(projectId));
    }

    @WebAspect
    @ApiOperation(value = "删除版本迭代", notes = "删除指定id的版本迭代")
    @ApiImplicitParam(name = "id", value = "版本迭代id", dataTypeClass = Integer.class)
    @DeleteMapping
    public RespModel delete(@RequestParam(name = "id") int id) {
        if (versionsService.delete(id)) {
            return new RespModel(RespEnum.DELETE_OK);
        } else {
            return new RespModel(RespEnum.ID_NOT_FOUND);
        }
    }

    @WebAspect
    @ApiOperation(value = "查询版本迭代信息", notes = "查询指定id的版本迭代的详细信息")
    @ApiImplicitParam(name = "id", value = "版本迭代id", dataTypeClass = Integer.class)
    @GetMapping
    public RespModel<Versions> findById(@RequestParam(name = "id") int id) {
        Versions versions = versionsService.findById(id);
        if (versions != null) {
            return new RespModel(RespEnum.SEARCH_OK, versions);
        } else {
            return new RespModel(RespEnum.ID_NOT_FOUND);
        }
    }
}
