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
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.PublicSteps;
import org.cloud.sonic.controller.models.domain.TestCases;
import org.cloud.sonic.controller.models.dto.PublicStepsDTO;
import org.cloud.sonic.controller.services.PublicStepsService;
import org.cloud.sonic.controller.services.TestCasesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "公共步骤相关")
@RestController
@RequestMapping("/publicSteps")
public class PublicStepsController {

    @Autowired
    private PublicStepsService publicStepsService;
    @Autowired
    private TestCasesService testCasesService;

    @WebAspect
    @ApiOperation(value = "查询公共步骤列表1", notes = "查找对应项目id下的公共步骤列表（分页）")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "projectId", value = "项目id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "page", value = "页码", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageSize", value = "页数据大小", dataTypeClass = Integer.class)
    })
    @GetMapping("/list")
    public RespModel<CommentPage<PublicStepsDTO>> findByProjectId(@RequestParam(name = "projectId") int projectId,
                                                                  @RequestParam(name = "page") int page,
                                                                  @RequestParam(name = "pageSize") int pageSize) {
        Page<PublicSteps> pageable = new Page<>(page, pageSize);
        return new RespModel<>(
                RespEnum.SEARCH_OK,
                publicStepsService.findByProjectId(projectId, pageable)
        );
    }

    @WebAspect
    @ApiOperation(value = "查询公共步骤列表2", notes = "查找对应项目id下的公共步骤列表（不分页，只查询id和name）")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "projectId", value = "项目id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "platform", value = "平台", dataTypeClass = Integer.class),
    })
    @GetMapping("/findNameByProjectId")
    public RespModel<List<Map<Integer, String>>> findByProjectId(@RequestParam(name = "projectId") int projectId,
                                                                 @RequestParam(name = "platform") int platform) {
        return new RespModel<>(RespEnum.SEARCH_OK, publicStepsService.findByProjectIdAndPlatform(projectId, platform));
    }

    @WebAspect
    @ApiOperation(value = "更新公共步骤信息", notes = "新增或更新公共步骤信息")
    @PutMapping
    public RespModel<String> save(@Validated @RequestBody PublicStepsDTO publicStepsDTO) {
        return new RespModel(RespEnum.UPDATE_OK, publicStepsService.savePublicSteps(publicStepsDTO));
    }

    @WebAspect
    @ApiOperation(value = "删除公共步骤", notes = "删除对应公共步骤id，包含的操作步骤不会被删除")
    @ApiImplicitParam(name = "id", value = "公共步骤id", dataTypeClass = Integer.class)
    @DeleteMapping
    public RespModel<String> delete(@RequestParam(name = "id") int id) {
        if (publicStepsService.delete(id)) {
            return new RespModel<>(RespEnum.DELETE_OK);
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }

    @WebAspect
    @ApiOperation(value = "删除公共步骤检查", notes = "返回引用公共步骤的用例")
    @ApiImplicitParam(name = "id", value = "公共步骤id", dataTypeClass = Integer.class)
    @GetMapping("deleteCheck")
    public RespModel<List<TestCases>> deleteCheck(@RequestParam(name = "id") int id) {
        return new RespModel<>(RespEnum.SEARCH_OK, testCasesService.listByPublicStepsId(id));
    }


    @WebAspect
    @ApiOperation(value = "查找公共步骤信息", notes = "查询对应公共步骤的详细信息")
    @ApiImplicitParam(name = "id", value = "公共步骤id", dataTypeClass = Integer.class)
    @GetMapping
    public RespModel<?> findById(@RequestParam(name = "id") int id) {
        PublicStepsDTO publicStepsDTO = publicStepsService.findById(id);
        if (publicStepsDTO != null) {
            return new RespModel<>(RespEnum.SEARCH_OK, publicStepsDTO);
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }
}
