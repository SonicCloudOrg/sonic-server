package com.sonic.controller.controller;

import com.sonic.common.config.WebAspect;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.controller.models.Projects;
import com.sonic.controller.services.ProjectsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des
 * @date 2021/9/9 22:46
 */
@Api(tags = "项目管理相关")
@RestController
@RequestMapping("/projects")
public class ProjectsController {
    @Autowired
    private ProjectsService projectsService;

    @WebAspect
    @ApiOperation(value = "更新项目信息", notes = "新增或更新项目信息")
    @PutMapping
    public RespModel save(@Validated @RequestBody Projects projects) {
        projectsService.save(projects);
        return new RespModel(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @ApiOperation(value = "查找所有项目", notes = "查找所有项目列表")
    @GetMapping("/list")
    public RespModel<List<Projects>> findAll() {
        return new RespModel(RespEnum.SEARCH_OK, projectsService.findAll());
    }

    @WebAspect
    @ApiOperation(value = "查询项目信息", notes = "查找对应id下的详细信息")
    @ApiImplicitParam(name = "id", value = "项目id", dataTypeClass = Integer.class)
    @GetMapping
    public RespModel<Projects> findById(@RequestParam(name = "id") int id) {
        Projects projects = projectsService.findById(id);
        if (projects != null) {
            return new RespModel(RespEnum.SEARCH_OK, projects);
        } else {
            return new RespModel(RespEnum.ID_NOT_FOUND);
        }
    }
}
