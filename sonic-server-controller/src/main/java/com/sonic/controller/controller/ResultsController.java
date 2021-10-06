package com.sonic.controller.controller;

import com.alibaba.fastjson.JSONArray;
import com.sonic.common.config.WebAspect;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.controller.models.Results;
import com.sonic.controller.services.ResultsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@Api(tags = "测试结果相关")
@RestController
@RequestMapping("/results")
public class ResultsController {
    @Autowired
    private ResultsService resultsService;

    @WebAspect
    @ApiOperation(value = "查询测试结果列表", notes = "查找对应项目id下的测试结果列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "projectId", value = "项目id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "page", value = "页码", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageSize", value = "页数据大小", dataTypeClass = Integer.class)
    })
    @GetMapping("/list")
    public RespModel<Page<Results>> findByProjectId(@RequestParam(name = "projectId") int projectId,
                                                    @RequestParam(name = "page") int page,
                                                    @RequestParam(name = "pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        return new RespModel(RespEnum.SEARCH_OK, resultsService.findByProjectId(projectId, pageable));
    }

    @WebAspect
    @ApiOperation(value = "删除测试结果", notes = "删除对应的测试结果id以及测试结果详情")
    @ApiImplicitParam(name = "id", value = "测试结果id", dataTypeClass = Integer.class)
    @DeleteMapping
    public RespModel delete(@RequestParam(name = "id") int id) {
        if (resultsService.delete(id)) {
            return new RespModel(RespEnum.DELETE_OK);
        } else {
            return new RespModel(RespEnum.ID_NOT_FOUND);
        }
    }

    @WebAspect
    @ApiOperation(value = "查询测试结果信息", notes = "查询对应id的测试结果信息")
    @ApiImplicitParam(name = "id", value = "测试结果id", dataTypeClass = Integer.class)
    @GetMapping
    public RespModel<Results> findById(@RequestParam(name = "id") int id) {
        return new RespModel(RespEnum.SEARCH_OK, resultsService.findById(id));
    }

    @WebAspect
    @GetMapping("/clean")
    public RespModel clean(@RequestParam(name = "day") int day) {
        resultsService.clean(day);
        return new RespModel(0, "开始清理测试结果！");
    }

    @WebAspect
    @GetMapping("/subResultCount")
    public RespModel subResultCount(@RequestParam(name = "id") int id) {
        resultsService.subResultCount(id);
        return new RespModel(RespEnum.HANDLE_OK);
    }

    @WebAspect
    @ApiOperation(value = "查询测试结果用例状态", notes = "查询对应id的测试结果用例状态")
    @ApiImplicitParam(name = "id", value = "测试结果id", dataTypeClass = Integer.class)
    @GetMapping("/findCaseStatus")
    public RespModel<JSONArray> findCaseStatus(@RequestParam(name = "id") int id) {
        JSONArray result = resultsService.findCaseStatus(id);
        if (result == null) {
            return new RespModel(RespEnum.ID_NOT_FOUND);
        } else {
            return new RespModel(RespEnum.SEARCH_OK, result);
        }
    }
}
