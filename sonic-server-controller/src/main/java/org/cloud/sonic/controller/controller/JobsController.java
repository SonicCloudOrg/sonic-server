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

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.exception.SonicException;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.Jobs;
import org.cloud.sonic.controller.models.dto.JobsDTO;
import org.cloud.sonic.controller.services.JobsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des 定时任务控制器
 * @date 2021/8/22 17:58
 */
@Tag(name = "定时任务相关")
@RestController
@RequestMapping("/jobs")
public class JobsController {

    @Autowired
    private JobsService jobsService;

    @WebAspect
    @Operation(summary = "更新定时任务信息", description = "新增或更新定时任务的信息")
    @PutMapping
    public RespModel<String> save(@Validated @RequestBody JobsDTO jobsDTO) throws SonicException {
        return jobsService.saveJobs(jobsDTO.convertTo());
    }

    @WebAspect
    @Operation(summary = "更改定时任务状态", description = "更改定时任务的状态")
    @Parameters(value = {
            @Parameter(name = "id", description = "定时任务id"),
            @Parameter(name = "type", description = "状态类型"),
    })
    @GetMapping("/updateStatus")
    public RespModel<String> updateStatus(@RequestParam(name = "id") int id, @RequestParam(name = "type") int type) {
        return jobsService.updateStatus(id, type);
    }

    @WebAspect
    @Operation(summary = "删除定时任务", description = "删除对应id的定时任务")
    @Parameter(name = "id", description = "定时任务id")
    @DeleteMapping
    public RespModel<String> delete(@RequestParam(name = "id") int id) {
        return jobsService.delete(id);
    }

    @WebAspect
    @Operation(summary = "查询定时任务列表", description = "查找对应项目id的定时任务列表")
    @Parameters(value = {
            @Parameter(name = "projectId", description = "项目id"),
            @Parameter(name = "page", description = "页码"),
            @Parameter(name = "pageSize", description = "页数据大小")
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
    @Operation(summary = "查询定时任务详细信息", description = "查找对应id的定时任务详细信息")
    @Parameter(name = "id", description = "定时任务id")
    @GetMapping
    public RespModel<Jobs> findById(@RequestParam(name = "id") int id) {
        Jobs jobs = jobsService.findById(id);
        if (jobs != null) {
            return new RespModel<>(RespEnum.SEARCH_OK, jobs);
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }

    @WebAspect
    @Operation(summary = "查询系统定时任务详细信息", description = "查找系统定时任务详细信息")
    @GetMapping("/findSysJobs")
    public RespModel<List<JSONObject>> findSysJobs() {
        return new RespModel<>(RespEnum.SEARCH_OK, jobsService.findSysJobs());
    }

    @WebAspect
    @Operation(summary = "更新系统定时任务", description = "更新系统定时任务")
    @Parameters(value = {
            @Parameter(name = "type", description = "类型"),
            @Parameter(name = "cron", description = "cron表达式")
    })
    @PutMapping("/updateSysJob")
    public RespModel updateSysJob(@RequestBody JSONObject jsonObject) {
        jobsService.updateSysJob(jsonObject.getString("type"), jsonObject.getString("cron"));
        return new RespModel<>(RespEnum.HANDLE_OK);
    }
}
