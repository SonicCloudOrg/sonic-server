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
import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.PublicSteps;
import org.cloud.sonic.controller.models.domain.TestCases;
import org.cloud.sonic.controller.models.dto.PublicStepsDTO;
import org.cloud.sonic.controller.services.PublicStepsService;
import org.cloud.sonic.controller.services.TestCasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "公共步骤相关")
@RestController
@RequestMapping("/publicSteps")
public class PublicStepsController {

    @Autowired
    private PublicStepsService publicStepsService;
    @Autowired
    private TestCasesService testCasesService;

    @WebAspect
    @Operation(summary = "查询公共步骤列表1", description = "查找对应项目id下的公共步骤列表（分页）")
    @Parameters(value = {
            @Parameter(name = "projectId", description = "项目id"),
            @Parameter(name = "page", description = "页码"),
            @Parameter(name = "pageSize", description = "页数据大小")
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
    @Operation(summary = "查询公共步骤列表2", description = "查找对应项目id下的公共步骤列表（不分页，只查询id和name）")
    @Parameters(value = {
            @Parameter(name = "projectId", description = "项目id"),
            @Parameter(name = "platform", description = "平台"),
    })
    @GetMapping("/findNameByProjectId")
    public RespModel<List<Map<String, Object>>> findByProjectId(@RequestParam(name = "projectId") int projectId,
                                                                @RequestParam(name = "platform") int platform) {
        return new RespModel<>(RespEnum.SEARCH_OK, publicStepsService.findByProjectIdAndPlatform(projectId, platform));
    }

    @WebAspect
    @Operation(summary = "更新公共步骤信息", description = "新增或更新公共步骤信息")
    @PutMapping
    public RespModel<String> save(@Validated @RequestBody PublicStepsDTO publicStepsDTO) {
        return new RespModel(RespEnum.UPDATE_OK, publicStepsService.savePublicSteps(publicStepsDTO));
    }

    @WebAspect
    @Operation(summary = "删除公共步骤", description = "删除对应公共步骤id，包含的操作步骤不会被删除")
    @Parameter(name = "id", description = "公共步骤id")
    @DeleteMapping
    public RespModel<String> delete(@RequestParam(name = "id") int id) {
        if (publicStepsService.delete(id)) {
            return new RespModel<>(RespEnum.DELETE_OK);
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }

    @WebAspect
    @Operation(summary = "删除公共步骤检查", description = "返回引用公共步骤的用例")
    @Parameter(name = "id", description = "公共步骤id")
    @GetMapping("deleteCheck")
    public RespModel<List<TestCases>> deleteCheck(@RequestParam(name = "id") int id) {
        return new RespModel<>(RespEnum.SEARCH_OK, testCasesService.listByPublicStepsId(id));
    }


    @WebAspect
    @Operation(summary = "查找公共步骤信息", description = "查询对应公共步骤的详细信息")
    @Parameter(name = "id", description = "公共步骤id")
    @GetMapping
    public RespModel<?> findById(@RequestParam(name = "id") int id) {
        PublicStepsDTO publicStepsDTO = publicStepsService.findById(id, false);
        if (publicStepsDTO != null) {
            return new RespModel<>(RespEnum.SEARCH_OK, publicStepsDTO);
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }

    @WebAspect
    @Operation(summary = "复制公共步骤", description = "复制对应公共步骤，步骤也会同步")
    @Parameter(name = "id", description = "公共步骤Id")
    @GetMapping("/copy")
    public RespModel<String> copyPublicSteps(@RequestParam(name = "id") int id) {
        publicStepsService.copyPublicSetpsIds(id);

        return new RespModel<>(RespEnum.COPY_OK);
    }
}
