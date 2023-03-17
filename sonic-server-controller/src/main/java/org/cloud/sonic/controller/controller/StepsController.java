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
import org.cloud.sonic.controller.mapper.PublicStepsMapper;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.PublicSteps;
import org.cloud.sonic.controller.models.domain.Steps;
import org.cloud.sonic.controller.models.dto.StepsDTO;
import org.cloud.sonic.controller.models.http.StepSort;
import org.cloud.sonic.controller.services.StepsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des
 * @date 2021/9/19 11:45
 */
@Tag(name = "操作步骤相关")
@RestController
@RequestMapping("/steps")
public class StepsController {
    @Autowired
    private StepsService stepsService;
    @Autowired
    private PublicStepsMapper publicStepsMapper;

    @WebAspect
    @Operation(summary = "查找步骤列表", description = "查找对应用例id下的步骤列表（分页）")
    @Parameters(value = {
            @Parameter(name = "projectId", description = "项目id"),
            @Parameter(name = "platform", description = "平台"),
            @Parameter(name = "page", description = "页码"),
            @Parameter(name = "pageSize", description = "页数据大小")
    })
    @GetMapping("/list")
    public RespModel<CommentPage<StepsDTO>> findAll(@RequestParam(name = "projectId") int projectId,
                                                    @RequestParam(name = "platform") int platform,
                                                    @RequestParam(name = "page") int page,
                                                    @RequestParam(name = "pageSize") int pageSize) {
        Page<Steps> pageable = new Page<>(page, pageSize);
        return new RespModel<>(RespEnum.SEARCH_OK, stepsService.findByProjectIdAndPlatform(projectId, platform, pageable));
    }

    @WebAspect
    @Operation(summary = "查找步骤列表", description = "查找对应用例id下的步骤列表")
    @Parameter(name = "caseId", description = "测试用例id")
    @GetMapping("/listAll")
    public RespModel<List<StepsDTO>> findByCaseIdOrderBySort(@RequestParam(name = "caseId") int caseId) {
        return new RespModel<>(RespEnum.SEARCH_OK, stepsService.findByCaseIdOrderBySort(caseId, false));
    }

    @WebAspect
    @Operation(summary = "移出测试用例", description = "将步骤从测试用例移出")
    @Parameter(name = "id", description = "步骤id")
    @GetMapping("/resetCaseId")
    public RespModel resetCaseId(@RequestParam(name = "id") int id) {
        if (stepsService.resetCaseId(id)) {
            return new RespModel<>(RespEnum.HANDLE_OK);
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }

    @WebAspect
    @Operation(summary = "删除操作步骤", description = "将步骤删除，并且从所有公共步骤里移除")
    @Parameter(name = "id", description = "步骤id")
    @DeleteMapping
    public RespModel<String> delete(@RequestParam(name = "id") int id) {
        if (stepsService.delete(id)) {
            return new RespModel<>(RespEnum.DELETE_OK);
        } else {
            return new RespModel<>(RespEnum.DELETE_FAIL);
        }
    }

    @WebAspect
    @Operation(summary = "删除操作步骤检查", description = "返回步骤所属的公共步骤")
    @Parameter(name = "id", description = "步骤id")
    @GetMapping("/deleteCheck")
    public RespModel<List<PublicSteps>> deleteCheck(@RequestParam(name = "id") int id) {
        return new RespModel<>(RespEnum.SEARCH_OK, publicStepsMapper.listPubStepsByStepId(id));
    }

    @WebAspect
    @Operation(summary = "更新操作步骤", description = "新增或更新操作步骤")
    @PutMapping
    public RespModel<String> save(@Validated @RequestBody StepsDTO stepsDTO) {
        stepsService.saveStep(stepsDTO);
        return new RespModel<>(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @Operation(summary = "拖拽排序步骤", description = "用于前端页面拖拽排序步骤")
    @PutMapping("/stepSort")
    public RespModel<String> stepSort(@Validated @RequestBody StepSort stepSort) {
        stepsService.sortSteps(stepSort);
        return new RespModel<>(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @Operation(summary = "查询步骤详情", description = "查询对应步骤id的详情信息")
    @Parameter(name = "id", description = "步骤id")
    @GetMapping
    public RespModel<?> findById(@RequestParam(name = "id") int id) {
        StepsDTO steps = stepsService.findById(id);
        if (steps != null) {
            return new RespModel<>(RespEnum.SEARCH_OK, steps);
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }

    @WebAspect
    @Operation(summary = "搜索查找步骤列表", description = "查找对应用例id下的步骤列表（分页）")
    @Parameters(value = {
            @Parameter(name = "projectId", description = "项目id"),
            @Parameter(name = "platform", description = "平台"),
            @Parameter(name = "page", description = "页码"),
            @Parameter(name = "pageSize", description = "页数据大小"),
            @Parameter(name = "searchContent", description = "搜索文本")
    })
    @GetMapping("/search/list")
    public RespModel<CommentPage<StepsDTO>> searchFindAll(@RequestParam(name = "projectId") int projectId,
                                                          @RequestParam(name = "platform") int platform,
                                                          @RequestParam(name = "page") int page,
                                                          @RequestParam(name = "pageSize") int pageSize,
                                                          @RequestParam(name = "searchContent") String searchContent) {
        return new RespModel<>(RespEnum.SEARCH_OK, stepsService.searchFindByProjectIdAndPlatform(projectId, platform,
                page, pageSize, searchContent));
    }

    @WebAspect
    @Operation(summary = "复制步骤", description = "测试用例复制其中一个步骤")
    @Parameters(value = {
            @Parameter(name = "id", description = "用例中需要被复制步骤Id"),
    })
    @GetMapping("/copy/steps")
    public RespModel<String> copyStepsIdByCase(@RequestParam(name = "id") int stepId) {
        stepsService.copyStepsIdByCase(stepId);

        return new RespModel<>(RespEnum.COPY_OK);
    }

    @WebAspect
    @Operation(summary = "开关步骤", description = "设置步骤的启用状态")
    @Parameters(value = {
            @Parameter(name = "id", description = "用例id"),
            @Parameter(name = "type", description = "状态"),
    })
    @GetMapping("/switchStep")
    public RespModel switchStep(@RequestParam(name = "id") int id, @RequestParam(name = "type") int type) {
        if (stepsService.switchStep(id, type)) {
            return new RespModel(RespEnum.HANDLE_OK);
        } else {
            return new RespModel(RespEnum.SEARCH_FAIL);
        }
    }

}
