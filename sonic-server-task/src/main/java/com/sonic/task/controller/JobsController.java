package com.sonic.task.controller;

import com.sonic.common.config.WebAspect;
import com.sonic.common.exception.SonicCronException;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.task.models.Jobs;
import com.sonic.task.service.JobsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public RespModel save(@Validated @RequestBody Jobs jobs) throws SonicCronException {
        return jobsService.save(jobs);
    }

    @WebAspect
    @ApiOperation(value = "更改定时任务状态", notes = "更改定时任务的状态")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "定时任务id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "type", value = "状态类型", dataTypeClass = Integer.class),
    })
    @GetMapping("/updateStatus")
    public RespModel updateStatus(@RequestParam(name = "id") int id, @RequestParam(name = "type") int type) {
        return jobsService.updateStatus(id, type);
    }

    @WebAspect
    @ApiOperation(value = "删除定时任务", notes = "删除对应id的定时任务")
    @ApiImplicitParam(name = "id", value = "定时任务id", dataTypeClass = Integer.class)
    @DeleteMapping
    public RespModel delete(@RequestParam(name = "id") int id) {
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
    public RespModel<Page<Jobs>> findByProjectId(@RequestParam(name = "projectId") int projectId
            , @RequestParam(name = "page") int page
            , @RequestParam(name = "pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        return new RespModel(RespEnum.SEARCH_OK, jobsService.findByProjectId(projectId, pageable));
    }

    @WebAspect
    @ApiOperation(value = "查询定时任务详细信息", notes = "查找对应id的定时任务详细信息")
    @ApiImplicitParam(name = "id", value = "定时任务id", dataTypeClass = Integer.class)
    @GetMapping
    public RespModel<Jobs> findById(@RequestParam(name = "id") int id) {
        Jobs jobs = jobsService.findById(id);
        if (jobs != null) {
            return new RespModel(RespEnum.SEARCH_OK, jobs);
        } else {
            return new RespModel(RespEnum.ID_NOT_FOUND);
        }
    }
}
