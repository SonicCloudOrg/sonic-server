/*
 *   sonic-server  Sonic Cloud Real Machine Platform.
 *   Copyright (C) 2022 SonicCloudOrg
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.cloud.sonic.controller.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.common.tools.JWTTokenTool;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.TestCases;
import org.cloud.sonic.controller.models.domain.TestSuites;
import org.cloud.sonic.controller.models.dto.TestCasesDTO;
import org.cloud.sonic.controller.models.dto.TestCasesQueryInfoDTO;
import org.cloud.sonic.controller.services.TestCasesService;
import org.cloud.sonic.controller.services.TestSuitesService;
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
            @ApiImplicitParam(name = "moduleIds", value = "模块Id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "page", value = "页码", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageSize", value = "页数据大小", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "orderAsc", value = "升序字段", dataTypeClass = String.class),
            @ApiImplicitParam(name = "orderDesc", value = "降序字段", dataTypeClass = String.class)
    })
    @GetMapping("/list")
    public RespModel<CommentPage<TestCasesDTO>> findAll(@RequestParam(name = "projectId") int projectId,
                                                        @RequestParam(name = "platform") int platform,
                                                        @RequestParam(name = "name", required = false) String name,
                                                        @RequestParam(name = "moduleIds[]", required = false) List<Integer> moduleIds,
                                                        @RequestParam(name = "page") int page,
                                                        @RequestParam(name = "pageSize") int pageSize,
                                                        @RequestParam(name = "idSort", required = false) String idSort,
                                                        @RequestParam(value = "designerSort", required = false) String designerSort,
                                                        @RequestParam(value = "editTimeSort", required = false, defaultValue = "desc") String editTimeSort) {
        Page<TestCases> pageable = new Page<>(page, pageSize);
        System.out.println(moduleIds);
        return new RespModel<>(
                RespEnum.SEARCH_OK,
                testCasesService.findAll(projectId, platform, name, moduleIds, pageable, idSort, designerSort, editTimeSort)
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
    public RespModel<TestCasesDTO> findById(@RequestParam(name = "id") int id) {
        TestCasesDTO testCases = testCasesService.findById(id);
        if (testCases != null) {
            return new RespModel<>(RespEnum.SEARCH_OK, testCases);
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
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

    //记得翻译
    @WebAspect
    @ApiOperation(value = "复制测试用例", notes = "复制对应用例id的用例详情")
    @ApiImplicitParam(name = "id", value = "用例id", dataTypeClass = Integer.class)
    @GetMapping("/copy")
    public RespModel<String> copyTestById(@RequestParam(name = "id") Integer id) {
        testCasesService.copyTestById(id);
        return new RespModel<>(RespEnum.COPY_OK);
    }
}
