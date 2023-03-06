///*
// *   sonic-server  Sonic Cloud Real Machine Platform.
// *   Copyright (C) 2022 SonicCloudOrg
// *
// *   This program is free software: you can redistribute it and/or modify
// *   it under the terms of the GNU Affero General Public License as published
// *   by the Free Software Foundation, either version 3 of the License, or
// *   (at your option) any later version.
// *
// *   This program is distributed in the hope that it will be useful,
// *   but WITHOUT ANY WARRANTY; without even the implied warranty of
// *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// *   GNU Affero General Public License for more details.
// *
// *   You should have received a copy of the GNU Affero General Public License
// *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
// */
//package org.cloud.sonic.controller.controller;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import org.cloud.sonic.common.config.WebAspect;
//import org.cloud.sonic.common.http.RespEnum;
//import org.cloud.sonic.common.http.RespModel;
//import org.cloud.sonic.controller.mapper.PublicStepsMapper;
//import org.cloud.sonic.controller.models.base.CommentPage;
//import org.cloud.sonic.controller.models.domain.PublicSteps;
//import org.cloud.sonic.controller.models.domain.Steps;
//import org.cloud.sonic.controller.models.dto.StepsDTO;
//import org.cloud.sonic.controller.models.http.StepSort;
//import org.cloud.sonic.controller.services.StepsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * @author ZhouYiXun
// * @des
// * @date 2021/9/19 11:45
// */
//@Tag(name = "操作步骤相关")
//@RestController
//@RequestMapping("/steps")
//public class StepsController {
//    @Autowired
//    private StepsService stepsService;
//    @Autowired
//    private PublicStepsMapper publicStepsMapper;
//
//    @WebAspect
//    @ApiOperation(value = "查找步骤列表", notes = "查找对应用例id下的步骤列表（分页）")
//    @ApiImplicitParams(value = {
//            @ApiImplicitParam(name = "projectId", value = "项目id", dataTypeClass = Integer.class),
//            @ApiImplicitParam(name = "platform", value = "平台", dataTypeClass = Integer.class),
//            @ApiImplicitParam(name = "page", value = "页码", dataTypeClass = Integer.class),
//            @ApiImplicitParam(name = "pageSize", value = "页数据大小", dataTypeClass = Integer.class)
//    })
//    @GetMapping("/list")
//    public RespModel<CommentPage<StepsDTO>> findAll(@RequestParam(name = "projectId") int projectId,
//                                                    @RequestParam(name = "platform") int platform,
//                                                    @RequestParam(name = "page") int page,
//                                                    @RequestParam(name = "pageSize") int pageSize) {
//        Page<Steps> pageable = new Page<>(page, pageSize);
//        return new RespModel<>(RespEnum.SEARCH_OK, stepsService.findByProjectIdAndPlatform(projectId, platform, pageable));
//    }
//
//    @WebAspect
//    @ApiOperation(value = "查找步骤列表", notes = "查找对应用例id下的步骤列表")
//    @ApiImplicitParam(name = "caseId", value = "测试用例id", dataTypeClass = Integer.class)
//    @GetMapping("/listAll")
//    public RespModel<List<StepsDTO>> findByCaseIdOrderBySort(@RequestParam(name = "caseId") int caseId) {
//        return new RespModel<>(RespEnum.SEARCH_OK, stepsService.findByCaseIdOrderBySort(caseId));
//    }
//
//    @WebAspect
//    @ApiOperation(value = "移出测试用例", notes = "将步骤从测试用例移出")
//    @ApiImplicitParam(name = "id", value = "步骤id", dataTypeClass = Integer.class)
//    @GetMapping("/resetCaseId")
//    public RespModel resetCaseId(@RequestParam(name = "id") int id) {
//        if (stepsService.resetCaseId(id)) {
//            return new RespModel<>(RespEnum.HANDLE_OK);
//        } else {
//            return new RespModel<>(RespEnum.ID_NOT_FOUND);
//        }
//    }
//
//    @WebAspect
//    @ApiOperation(value = "删除操作步骤", notes = "将步骤删除，并且从所有公共步骤里移除")
//    @ApiImplicitParam(name = "id", value = "步骤id", dataTypeClass = Integer.class)
//    @DeleteMapping
//    public RespModel<String> delete(@RequestParam(name = "id") int id) {
//        if (stepsService.delete(id)) {
//            return new RespModel<>(RespEnum.DELETE_OK);
//        } else {
//            return new RespModel<>(RespEnum.DELETE_FAIL);
//        }
//    }
//
//    @WebAspect
//    @ApiOperation(value = "删除操作步骤检查", notes = "返回步骤所属的公共步骤")
//    @ApiImplicitParam(name = "id", value = "步骤id", dataTypeClass = Integer.class)
//    @GetMapping("/deleteCheck")
//    public RespModel<List<PublicSteps>> deleteCheck(@RequestParam(name = "id") int id) {
//        return new RespModel<>(RespEnum.SEARCH_OK, publicStepsMapper.listPubStepsByStepId(id));
//    }
//
//    @WebAspect
//    @ApiOperation(value = "更新操作步骤", notes = "新增或更新操作步骤")
//    @PutMapping
//    public RespModel<String> save(@Validated @RequestBody StepsDTO stepsDTO) {
//        stepsService.saveStep(stepsDTO);
//        return new RespModel<>(RespEnum.UPDATE_OK);
//    }
//
//    @WebAspect
//    @ApiOperation(value = "拖拽排序步骤", notes = "用于前端页面拖拽排序步骤")
//    @PutMapping("/stepSort")
//    public RespModel<String> stepSort(@Validated @RequestBody StepSort stepSort) {
//        stepsService.sortSteps(stepSort);
//        return new RespModel<>(RespEnum.UPDATE_OK);
//    }
//
//    @WebAspect
//    @ApiOperation(value = "查询步骤详情", notes = "查询对应步骤id的详情信息")
//    @ApiImplicitParam(name = "id", value = "步骤id", dataTypeClass = Integer.class)
//    @GetMapping
//    public RespModel<?> findById(@RequestParam(name = "id") int id) {
//        StepsDTO steps = stepsService.findById(id);
//        if (steps != null) {
//            return new RespModel<>(RespEnum.SEARCH_OK, steps);
//        } else {
//            return new RespModel<>(RespEnum.ID_NOT_FOUND);
//        }
//    }
//
//    @WebAspect
//    @ApiOperation(value = "搜索查找步骤列表", notes = "查找对应用例id下的步骤列表（分页）")
//    @ApiImplicitParams(value = {
//            @ApiImplicitParam(name = "projectId", value = "项目id", dataTypeClass = Integer.class),
//            @ApiImplicitParam(name = "platform", value = "平台", dataTypeClass = Integer.class),
//            @ApiImplicitParam(name = "page", value = "页码", dataTypeClass = Integer.class),
//            @ApiImplicitParam(name = "pageSize", value = "页数据大小", dataTypeClass = Integer.class),
//            @ApiImplicitParam(name = "searchContent", value = "搜索文本", dataTypeClass = String.class)
//    })
//    @GetMapping("/search/list")
//    public RespModel<CommentPage<StepsDTO>> searchFindAll(@RequestParam(name = "projectId") int projectId,
//                                                          @RequestParam(name = "platform") int platform,
//                                                          @RequestParam(name = "page") int page,
//                                                          @RequestParam(name = "pageSize") int pageSize,
//                                                          @RequestParam(name = "searchContent") String searchContent) {
//        return new RespModel<>(RespEnum.SEARCH_OK, stepsService.searchFindByProjectIdAndPlatform(projectId, platform,
//                page, pageSize, searchContent));
//    }
//
//    @WebAspect
//    @ApiOperation(value = "复制步骤", notes = "测试用例复制其中一个步骤")
//    @ApiImplicitParams(value = {
//            @ApiImplicitParam(name = "id", value = "用例中需要被复制步骤Id", dataTypeClass = Integer.class),
//    })
//    @GetMapping("/copy/steps")
//    public RespModel<String> copyStepsIdByCase(@RequestParam(name = "id") int stepId) {
//        stepsService.copyStepsIdByCase(stepId);
//
//        return new RespModel<>(RespEnum.COPY_OK);
//    }
//
//}
