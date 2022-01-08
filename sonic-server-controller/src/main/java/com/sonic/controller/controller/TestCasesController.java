package com.sonic.controller.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sonic.common.config.WebAspect;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.common.tools.JWTTokenTool;
import com.sonic.controller.models.base.CommentPage;
import com.sonic.controller.models.domain.TestCases;
import com.sonic.controller.models.domain.TestSuites;
import com.sonic.controller.models.dto.TestCasesDTO;
import com.sonic.controller.services.TestCasesService;
import com.sonic.controller.services.TestSuitesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private JWTTokenTool jwtTokenTool;
    @Autowired
    private TestSuitesService testSuitesService;

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
    public RespModel<CommentPage<TestCases>> findAll(@RequestParam(name = "projectId") int projectId,
                                                     @RequestParam(name = "platform") int platform,
                                                     @RequestParam(name = "name") String name,
                                                     @RequestParam(name = "page") int page,
                                                     @RequestParam(name = "pageSize") int pageSize) {
        Page<TestCases> pageable = new Page<>(page, pageSize);
        return new RespModel<>(
                RespEnum.SEARCH_OK,
                CommentPage.convertFrom(testCasesService.findAll(projectId, platform, name, pageable))
        );
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
        return new RespModel<>(RespEnum.SEARCH_OK,
                testCasesService.findAll(projectId, platform));
    }

    @WebAspect
    @ApiOperation(value = "删除测试用例", notes = "删除对应用例id，用例下的操作步骤的caseId重置为0")
    @ApiImplicitParam(name = "id", value = "用例id", dataTypeClass = Integer.class)
    @DeleteMapping
    public RespModel<String> delete(@RequestParam(name = "id") int id) {
        if (testCasesService.delete(id)) {
            return new RespModel<>(RespEnum.DELETE_OK);
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }

    @WebAspect
    @ApiOperation(value = "删除测试用例检查", notes = "返回被引用的测试套件")
    @ApiImplicitParam(name = "id", value = "用例id", dataTypeClass = Integer.class)
    @GetMapping("deleteCheck")
    public RespModel<List<TestSuites>> deleteCheck(@RequestParam(name = "id") int id) {
        return new RespModel<>(RespEnum.SEARCH_OK, testSuitesService.listTestSuitesByTestCasesId(id));
    }

    @WebAspect
    @ApiOperation(value = "更新测试用例信息", notes = "新增或更改测试用例信息")
    @PutMapping
    public RespModel<String> save(@Validated @RequestBody TestCasesDTO testCasesDTO, HttpServletRequest request) {
        if (request.getHeader("SonicToken") != null) {
            String token = request.getHeader("SonicToken");
            String userName = jwtTokenTool.getUserName(token);
            if (userName != null) {
                testCasesDTO.setDesigner(userName);
            }
        }
        testCasesService.save(testCasesDTO.convertTo());
        return new RespModel<>(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @ApiOperation(value = "查询测试用例详情", notes = "查找对应用例id的用例详情")
    @ApiImplicitParam(name = "id", value = "用例id", dataTypeClass = Integer.class)
    @GetMapping
    public RespModel<TestCases> findById(@RequestParam(name = "id") int id) {
        TestCases testCases = testCasesService.findById(id);
        if (testCases != null) {
            return new RespModel<>(RespEnum.SEARCH_OK, testCases);
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }

    @WebAspect
    @GetMapping("/findSteps")
    public RespModel<JSONObject> findSteps(@RequestParam(name = "id") int id) {
        JSONObject jsonObject = testCasesService.findSteps(id);
        if (jsonObject != null) {
            return new RespModel<>(RespEnum.SEARCH_OK, jsonObject);
        } else {
            return new RespModel<>(-1, "查询出错！");
        }
    }

    @WebAspect
    @ApiOperation(value = "批量查询用例", notes = "查找id列表的用例信息，可以传多个ids[]")
    @ApiImplicitParam(name = "ids[]", value = "id列表", dataTypeClass = Integer.class)
    @GetMapping("/findByIdIn")
    public RespModel<List<TestCases>> findByIdIn(@RequestParam(name = "ids[]") List<Integer> ids) {
        return new RespModel<>(RespEnum.SEARCH_OK,
                testCasesService.findByIdIn(ids));
    }
}
