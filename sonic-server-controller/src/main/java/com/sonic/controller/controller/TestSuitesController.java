package com.sonic.controller.controller;

import com.sonic.common.config.WebAspect;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.controller.models.TestSuites;
import com.sonic.controller.services.TestSuitesService;
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

@Api(tags = "测试套件相关")
@RestController
@RequestMapping("/testSuites")
public class TestSuitesController {
    @Autowired
    private TestSuitesService testSuitesService;

    @WebAspect
    @ApiOperation(value = "运行测试套件", notes = "运行指定项目的指定测试套件")
    @ApiImplicitParam(name = "id", value = "测试套件id", dataTypeClass = Integer.class)
    @GetMapping("/runSuite")
    public RespModel runSuite(@RequestParam(name = "id") int id
            , HttpServletRequest request) {
        String strike = "SYSTEM";
        if (request.getHeader("sonicToken") != null) {
            String token = request.getHeader("sonicToken");
            if (RedisTool.get("sonic:user:" + token) != null) {
                strike = RedisTool.get("sonic:user:" + token).toString();
            }
        }
        return testSuitesService.runSuite(id, strike);
    }

    @WebAspect
    @ApiOperation(value = "删除测试套件", notes = "删除指定id的测试套件")
    @ApiImplicitParam(name = "id", value = "测试套件id", dataTypeClass = Integer.class)
    @DeleteMapping
    public RespModel delete(@RequestParam(name = "id") int id) {
        if (testSuitesService.delete(id)) {
            return new RespModel(RespEnum.DELETE_OK);
        } else {
            return new RespModel(RespEnum.ID_NOT_FOUND);
        }
    }

    @WebAspect
    @ApiOperation(value = "更新测试套件", notes = "更新或新增测试套件")
    @PutMapping
    public RespModel save(@Validated @RequestBody TestSuites testSuites) {
        testSuitesService.save(testSuites);
        return new RespModel(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @ApiOperation(value = "查询测试套件列表", notes = "用于查询对应项目id下的测试套件列表")
    @ApiImplicitParam(name = "projectId", value = "项目id", dataTypeClass = Integer.class)
    @GetMapping("/list")
    public RespModel<Page<TestSuites>> findByProjectId(@RequestParam(name = "projectId") int projectId
            , @RequestParam(name = "name") String name
            , @RequestParam(name = "page") int page
            , @RequestParam(name = "pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return new RespModel(RespEnum.SEARCH_OK, testSuitesService.findByProjectId(projectId, name, pageable));
    }

    @WebAspect
    @ApiOperation(value = "查询测试套件列表", notes = "用于查询对应项目id下的测试套件列表(不分页)")
    @ApiImplicitParam(name = "projectId", value = "项目id", dataTypeClass = Integer.class)
    @GetMapping("/listAll")
    public RespModel<List<TestSuites>> findByProjectId(@RequestParam(name = "projectId") int projectId) {
        return new RespModel(RespEnum.SEARCH_OK, testSuitesService.findByProjectId(projectId));
    }

    @WebAspect
    @ApiOperation(value = "测试套件详情", notes = "查看测试套件的配置信息详情")
    @ApiImplicitParam(name = "id", value = "测试套件id", dataTypeClass = Integer.class)
    @GetMapping
    public RespModel<TestSuites> findById(@RequestParam(name = "id") int id) {
        TestSuites testSuites = testSuitesService.findById(id);
        if (testSuites != null) {
            return new RespModel(RespEnum.SEARCH_OK, testSuites);
        } else {
            return new RespModel(RespEnum.ID_NOT_FOUND);
        }
    }
}
