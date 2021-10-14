package com.sonic.controller.controller;

import com.alibaba.fastjson.JSONObject;
import com.sonic.common.config.WebAspect;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.controller.models.TestCases;
import com.sonic.controller.models.Users;
import com.sonic.controller.services.TestCasesService;
import com.sonic.controller.tools.RedisTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "测试用例相关")
@RestController
@RequestMapping("/testCases")
public class TestCasesController {
    @Autowired
    private TestCasesService testCasesService;

    @WebAspect
    @ApiOperation(value = "查询测试用例列表", notes = "查找对应项目id下的测试用例列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "projectId", value = "项目id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "platform", value = "平台类型", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "name", value = "用例名称", dataTypeClass = String.class),
            @ApiImplicitParam(name = "page", value = "页码", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageSize", value = "页数据大小", dataTypeClass = Integer.class)
    })
    @GetMapping("/list")
    public RespModel<Page<TestCases>> findAll(@RequestParam(name = "projectId") int projectId,
                                              @RequestParam(name = "platform") int platform,
                                              @RequestParam(name = "name") String name,
                                              @RequestParam(name = "page") int page,
                                              @RequestParam(name = "pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "editTime"));
        return new RespModel(RespEnum.SEARCH_OK,
                testCasesService.findAll(projectId, platform, name, pageable));
    }

    @WebAspect
    @ApiOperation(value = "查询测试用例列表", notes = "不分页的测试用例列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "projectId", value = "项目id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "platform", value = "平台类型", dataTypeClass = Integer.class),
    })
    @GetMapping("/listAll")
    public RespModel<List<TestCases>> findAll(@RequestParam(name = "projectId") int projectId,
                                              @RequestParam(name = "platform") int platform) {
        return new RespModel(RespEnum.SEARCH_OK,
                testCasesService.findAll(projectId, platform));
    }

    @WebAspect
    @ApiOperation(value = "删除测试用例", notes = "删除对应用例id，用例下的操作步骤的caseId重置为0")
    @ApiImplicitParam(name = "id", value = "用例id", dataTypeClass = Integer.class)
    @DeleteMapping
    public RespModel delete(@RequestParam(name = "id") int id) {
        if (testCasesService.delete(id)) {
            return new RespModel(RespEnum.DELETE_OK);
        } else {
            return new RespModel(RespEnum.ID_NOT_FOUND);
        }
    }

    @WebAspect
    @ApiOperation(value = "更新测试用例信息", notes = "新增或更改测试用例信息")
    @PutMapping
    public RespModel save(@Validated @RequestBody TestCases testCases, HttpServletRequest request) {
        if (request.getHeader("SonicToken") != null) {
            String token = request.getHeader("SonicToken");
            Object t = RedisTool.get("sonic:user:" + token);
            if (t != null) {
                String userName = ((Users) t).getUserName();
                testCases.setDesigner(userName);
            }
        }
        testCasesService.save(testCases);
        return new RespModel(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @ApiOperation(value = "查询测试用例详情", notes = "查找对应用例id的用例详情")
    @ApiImplicitParam(name = "id", value = "用例id", dataTypeClass = Integer.class)
    @GetMapping
    public RespModel<TestCases> findById(@RequestParam(name = "id") int id) {
        TestCases testCases = testCasesService.findById(id);
        if (testCases != null) {
            return new RespModel(RespEnum.SEARCH_OK, testCases);
        } else {
            return new RespModel(RespEnum.ID_NOT_FOUND);
        }
    }

    @WebAspect
    @GetMapping("/findSteps")
    public RespModel<JSONObject> findSteps(@RequestParam(name = "id") int id) {
        JSONObject jsonObject = testCasesService.findSteps(id);
        if (jsonObject != null) {
            return new RespModel(RespEnum.SEARCH_OK, jsonObject);
        } else {
            return new RespModel(-1, "查询出错！");
        }
    }

    @WebAspect
    @ApiOperation(value = "批量查询用例", notes = "查找id列表的用例信息，可以传多个ids[]")
    @ApiImplicitParam(name = "ids[]", value = "id列表", dataTypeClass = Integer.class)
    @GetMapping("/findByIdIn")
    public RespModel<List<TestCases>> findByIdIn(@RequestParam(name = "ids[]") List<Integer> ids) {
        return new RespModel(RespEnum.SEARCH_OK,
                testCasesService.findByIdIn(ids));
    }
}
