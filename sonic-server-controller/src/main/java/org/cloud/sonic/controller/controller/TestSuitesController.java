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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.common.tools.JWTTokenTool;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.TestSuites;
import org.cloud.sonic.controller.models.dto.TestSuitesDTO;
import org.cloud.sonic.controller.services.TestSuitesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "测试套件相关")
@RestController
@RequestMapping("/testSuites")
public class TestSuitesController {
    @Autowired
    private TestSuitesService testSuitesService;
    @Autowired
    private JWTTokenTool jwtTokenTool;

    @WebAspect
    @Operation(summary = "运行测试套件", description = "运行指定项目的指定测试套件")
    @Parameter(name = "id", description = "测试套件id")
    @GetMapping("/runSuite")
    public RespModel<Integer> runSuite(@RequestParam(name = "id") int id
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
    @Operation(summary = "停止测试套件运行", description = "停止测试套件运行")
    @Parameter(name = "resultId", description = "测试结果Id")
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
    @Operation(summary = "删除测试套件", description = "删除指定id的测试套件")
    @Parameter(name = "id", description = "测试套件id")
    @DeleteMapping
    public RespModel<String> delete(@RequestParam(name = "id") int id) {
        if (testSuitesService.delete(id)) {
            return new RespModel<>(RespEnum.DELETE_OK);
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }

    @WebAspect
    @Operation(summary = "更新测试套件", description = "更新或新增测试套件")
    @PutMapping
    public RespModel<String> save(@Validated @RequestBody TestSuitesDTO testSuitesDTO) {
        testSuitesService.saveTestSuites(testSuitesDTO);
        return new RespModel<>(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @Operation(summary = "查询测试套件列表", description = "用于查询对应项目id下的测试套件列表")
    @Parameter(name = "projectId", description = "项目id")
    @GetMapping("/list")
    public RespModel<CommentPage<TestSuitesDTO>> findByProjectId(@RequestParam(name = "projectId") int projectId
            , @RequestParam(name = "name") String name
            , @RequestParam(name = "page") int page
            , @RequestParam(name = "pageSize") int pageSize) {
        Page<TestSuites> pageable = new Page<>(page, pageSize);
        return new RespModel<>(RespEnum.SEARCH_OK, testSuitesService.findByProjectId(projectId, name, pageable));
    }

    @WebAspect
    @Operation(summary = "查询测试套件列表", description = "用于查询对应项目id下的测试套件列表(不分页)")
    @Parameter(name = "projectId", description = "项目id")
    @GetMapping("/listAll")
    public RespModel<List<TestSuitesDTO>> findByProjectId(@RequestParam(name = "projectId") int projectId) {
        return new RespModel<>(RespEnum.SEARCH_OK, testSuitesService.findByProjectId(projectId));
    }

    @WebAspect
    @Operation(summary = "测试套件详情", description = "查看测试套件的配置信息详情")
    @Parameter(name = "id", description = "测试套件id")
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
