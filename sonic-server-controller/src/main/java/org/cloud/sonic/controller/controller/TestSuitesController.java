/*
 *  Copyright (C) [SonicCloudOrg] Sonic Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.cloud.sonic.controller.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.common.tools.JWTTokenTool;
import org.cloud.sonic.common.models.base.CommentPage;
import org.cloud.sonic.common.models.domain.TestSuites;
import org.cloud.sonic.common.models.dto.TestSuitesDTO;
import org.cloud.sonic.common.services.TestSuitesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private JWTTokenTool jwtTokenTool;

    @WebAspect
    @ApiOperation(value = "运行测试套件", notes = "运行指定项目的指定测试套件")
    @ApiImplicitParam(name = "id", value = "测试套件id", dataTypeClass = Integer.class)
    @GetMapping("/runSuite")
    public RespModel<String> runSuite(@RequestParam(name = "id") int id
            , HttpServletRequest request) {
        String strike = "SYSTEM";
        if (request.getHeader("SonicToken") != null) {
            String token = request.getHeader("SonicToken");
            String userName = jwtTokenTool.getUserName(token);
            if (userName != null) {
                strike = userName;
            }
        }
        return testSuitesService.runSuite(id, strike);
    }

    @WebAspect
    @ApiOperation(value = "停止测试套件运行", notes = "停止测试套件运行")
    @ApiImplicitParam(name = "resultId", value = "测试结果Id", dataTypeClass = Integer.class)
    @GetMapping("/forceStopSuite")
    public RespModel<String> forceStopSuite(@RequestParam(name = "resultId") int resultId
            , HttpServletRequest request) {
        String strike = "SYSTEM";
        if (request.getHeader("SonicToken") != null) {
            String token = request.getHeader("SonicToken");
            String userName = jwtTokenTool.getUserName(token);
            if (userName != null) {
                strike = userName;
            }
        }
        return testSuitesService.forceStopSuite(resultId, strike);
    }


    @WebAspect
    @ApiOperation(value = "删除测试套件", notes = "删除指定id的测试套件")
    @ApiImplicitParam(name = "id", value = "测试套件id", dataTypeClass = Integer.class)
    @DeleteMapping
    public RespModel<String> delete(@RequestParam(name = "id") int id) {
        if (testSuitesService.delete(id)) {
            return new RespModel<>(RespEnum.DELETE_OK);
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }

    @WebAspect
    @ApiOperation(value = "更新测试套件", notes = "更新或新增测试套件")
    @PutMapping
    public RespModel<String> save(@Validated @RequestBody TestSuitesDTO testSuitesDTO) {
        testSuitesService.saveTestSuites(testSuitesDTO);
        return new RespModel<>(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @ApiOperation(value = "查询测试套件列表", notes = "用于查询对应项目id下的测试套件列表")
    @ApiImplicitParam(name = "projectId", value = "项目id", dataTypeClass = Integer.class)
    @GetMapping("/list")
    public RespModel<CommentPage<TestSuitesDTO>> findByProjectId(@RequestParam(name = "projectId") int projectId
            , @RequestParam(name = "name") String name
            , @RequestParam(name = "page") int page
            , @RequestParam(name = "pageSize") int pageSize) {
        Page<TestSuites> pageable = new Page<>(page, pageSize);
        return new RespModel<>(RespEnum.SEARCH_OK, testSuitesService.findByProjectId(projectId, name, pageable));
    }

    @WebAspect
    @ApiOperation(value = "查询测试套件列表", notes = "用于查询对应项目id下的测试套件列表(不分页)")
    @ApiImplicitParam(name = "projectId", value = "项目id", dataTypeClass = Integer.class)
    @GetMapping("/listAll")
    public RespModel<List<TestSuitesDTO>> findByProjectId(@RequestParam(name = "projectId") int projectId) {
        return new RespModel<>(RespEnum.SEARCH_OK, testSuitesService.findByProjectId(projectId));
    }

    @WebAspect
    @ApiOperation(value = "测试套件详情", notes = "查看测试套件的配置信息详情")
    @ApiImplicitParam(name = "id", value = "测试套件id", dataTypeClass = Integer.class)
    @GetMapping
    public RespModel<?> findById(@RequestParam(name = "id") int id) {
        TestSuitesDTO testSuitesDTO = testSuitesService.findById(id);
        if (testSuitesDTO != null) {
            return new RespModel<>(RespEnum.SEARCH_OK, testSuitesDTO);
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }
}
