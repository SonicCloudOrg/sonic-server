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
import org.cloud.sonic.common.exception.SonicException;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.services.JobsService;
import org.cloud.sonic.controller.models.domain.Jobs;
import org.cloud.sonic.controller.models.dto.JobsDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author ZhouYiXun
 * @des 定时任务控制器
 * @date 2021/8/22 17:58
 */
@Api(tags = "定时任务")
@RestController
@RequestMapping("/jobs")
public class JobsController {

    @Autowired
    private JobsService jobsService;

    @WebAspect
    @ApiOperation(value = "更新定时任务信息", notes = "新增或更新定时任务的信息")
    @PutMapping
    public RespModel<String> save(@Validated @RequestBody JobsDTO jobsDTO) throws SonicException {
        return jobsService.saveJobs(jobsDTO.convertTo());
    }

    @WebAspect
    @ApiOperation(value = "更改定时任务状态", notes = "更改定时任务的状态")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "定时任务id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "type", value = "状态类型", dataTypeClass = Integer.class),
    })
    @GetMapping("/updateStatus")
    public RespModel<String> updateStatus(@RequestParam(name = "id") int id, @RequestParam(name = "type") int type) {
        return jobsService.updateStatus(id, type);
    }

    @WebAspect
    @ApiOperation(value = "删除定时任务", notes = "删除对应id的定时任务")
    @ApiImplicitParam(name = "id", value = "定时任务id", dataTypeClass = Integer.class)
    @DeleteMapping
    public RespModel<String> delete(@RequestParam(name = "id") int id) {
        return jobsService.delete(id);
    }

    @WebAspect
    @ApiOperation(value = "查询定时任务列表", notes = "查找对应项目id的定时任务列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "projectId", value = "项目id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "page", value = "页码", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageSize", value = "页数据大小", dataTypeClass = Integer.class)
    })
    @GetMapping("/list")
    public RespModel<CommentPage<Jobs>> findByProjectId(@RequestParam(name = "projectId") int projectId
            , @RequestParam(name = "page") int page
            , @RequestParam(name = "pageSize") int pageSize) {
        Page<Jobs> pageable = new Page<>(page, pageSize);
        return new RespModel<>(
                RespEnum.SEARCH_OK,
                CommentPage.convertFrom(jobsService.findByProjectId(projectId, pageable))
        );
    }

    @WebAspect
    @ApiOperation(value = "查询定时任务详细信息", notes = "查找对应id的定时任务详细信息")
    @ApiImplicitParam(name = "id", value = "定时任务id", dataTypeClass = Integer.class)
    @GetMapping
    public RespModel<Jobs> findById(@RequestParam(name = "id") int id) {
        Jobs jobs = jobsService.findById(id);
        if (jobs != null) {
            return new RespModel<>(RespEnum.SEARCH_OK, jobs);
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }
}
