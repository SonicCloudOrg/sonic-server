/*
 *   sonic-server  Sonic Cloud Real Machine Platform.
 *   Copyright (C) 2022 SonicCloudOrg
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.cloud.sonic.controller.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.common.tools.JWTTokenTool;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.TestCases;
import org.cloud.sonic.controller.models.domain.TestSuites;
import org.cloud.sonic.controller.models.dto.TestCasesDTO;
import org.cloud.sonic.controller.services.TestCasesService;
import org.cloud.sonic.controller.services.TestSuitesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Tag(name = "测试用例相关")
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
    @Operation(summary = "查询测试用例列表", description = "查找对应项目id下的测试用例列表")
    @Parameters(value = {
            @Parameter(name = "projectId", description = "项目id"),
            @Parameter(name = "platform", description = "平台类型"),
            @Parameter(name = "name", description = "用例名称"),
            @Parameter(name = "moduleIds", description = "模块Id"),
            @Parameter(name = "caseAuthorNames", description = "用例作者列表"),
            @Parameter(name = "page", description = "页码"),
            @Parameter(name = "pageSize", description = "页数据大小"),
            @Parameter(name = "idSort", description = "控制id排序方式"),
            @Parameter(name = "editTimeSort", description = "控制editTime排序方式")

    })
    @GetMapping("/list")
    public RespModel<CommentPage<TestCasesDTO>> findAll(@RequestParam(name = "projectId") int projectId,
                                                        @RequestParam(name = "platform") int platform,
                                                        @RequestParam(name = "name", required = false) String name,
                                                        @RequestParam(name = "moduleIds[]", required = false) List<Integer> moduleIds,
                                                        @RequestParam(name = "caseAuthorNames[]", required = false) List<String> caseAuthorNames,
                                                        @RequestParam(name = "page") int page,
                                                        @RequestParam(name = "pageSize") int pageSize,
                                                        @RequestParam(name = "idSort", required = false) String idSort,
                                                        @RequestParam(value = "editTimeSort", required = false) String editTimeSort) {
        Page<TestCases> pageable = new Page<>(page, pageSize);
        return new RespModel<>(
                RespEnum.SEARCH_OK,
                testCasesService.findAll(projectId, platform, name, moduleIds, caseAuthorNames,
                        pageable, idSort, editTimeSort)
        );
    }

    @WebAspect
    @Operation(summary = "查询测试用例列表", description = "不分页的测试用例列表")
    @Parameters(value = {
            @Parameter(name = "projectId", description = "项目id"),
            @Parameter(name = "platform", description = "平台类型"),
    })
    @GetMapping("/listAll")
    public RespModel<List<TestCases>> findAll(@RequestParam(name = "projectId") int projectId,
                                              @RequestParam(name = "platform") int platform) {
        return new RespModel<>(RespEnum.SEARCH_OK,
                testCasesService.findAll(projectId, platform));
    }

    @WebAspect
    @Operation(summary = "删除测试用例", description = "删除对应用例id，用例下的操作步骤的caseId重置为0")
    @Parameter(name = "id", description = "用例id")
    @DeleteMapping
    public RespModel<String> delete(@RequestParam(name = "id") int id) {
        if (testCasesService.delete(id)) {
            return new RespModel<>(RespEnum.DELETE_OK);
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }

    @WebAspect
    @Operation(summary = "删除测试用例检查", description = "返回被引用的测试套件")
    @Parameter(name = "id", description = "用例id")
    @GetMapping("deleteCheck")
    public RespModel<List<TestSuites>> deleteCheck(@RequestParam(name = "id") int id) {
        return new RespModel<>(RespEnum.SEARCH_OK, testSuitesService.listTestSuitesByTestCasesId(id));
    }

    @WebAspect
    @Operation(summary = "更新测试用例信息", description = "新增或更改测试用例信息")
    @PutMapping
    public RespModel<String> save(@Validated @RequestBody TestCasesDTO testCasesDTO, HttpServletRequest request) {
        if (request.getHeader("SonicToken") != null) {
            String token = request.getHeader("SonicToken");
            String userName = jwtTokenTool.getUserName(token);
            if (userName != null) {
                testCasesDTO.setDesigner(userName);
            }
        }

        // 修改时，更新修改时间
        if (!StringUtils.isEmpty(testCasesDTO.getId())) {
            testCasesDTO.setEditTime(new Date());
        }
        testCasesService.save(testCasesDTO.convertTo());
        return new RespModel<>(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @Operation(summary = "查询测试用例详情", description = "查找对应用例id的用例详情")
    @Parameter(name = "id", description = "用例id")
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
    @Operation(summary = "批量查询用例", description = "查找id列表的用例信息，可以传多个ids[]")
    @Parameter(name = "ids[]", description = "id列表")
    @GetMapping("/findByIdIn")
    public RespModel<List<TestCases>> findByIdIn(@RequestParam(name = "ids[]") List<Integer> ids) {
        return new RespModel<>(RespEnum.SEARCH_OK,
                testCasesService.findByIdIn(ids));
    }

    //记得翻译
    @WebAspect
    @Operation(summary = "复制测试用例", description = "复制对应用例id的用例详情")
    @Parameter(name = "id", description = "用例id")
    @GetMapping("/copy")
    public RespModel<String> copyTestById(@RequestParam(name = "id") Integer id) {
        testCasesService.copyTestById(id);
        return new RespModel<>(RespEnum.COPY_OK);
    }

    @WebAspect
    @Operation(summary = "查询用例所有的作者列表", description = "查找对应项目id下对应平台的所有作者列表")
    @Parameters(value = {
            @Parameter(name = "projectId", description = "项目id"),
            @Parameter(name = "platform", description = "平台类型"),
    })
    @GetMapping("/listAllCaseAuthor")
    public RespModel<List<String>> findAllCaseAuthor(@RequestParam(name = "projectId") int projectId,
                                                     @RequestParam(name = "platform") int platform) {
        return new RespModel<>(
                RespEnum.SEARCH_OK,
                testCasesService.findAllCaseAuthor(projectId, platform)
        );
    }
}
