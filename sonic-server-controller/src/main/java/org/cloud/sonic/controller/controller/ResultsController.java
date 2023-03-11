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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.Results;
import org.cloud.sonic.controller.services.ResultsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "测试结果相关")
@RestController
@RequestMapping("/results")
public class ResultsController {
    @Autowired
    private ResultsService resultsService;

    @WebAspect
    @Operation(summary = "查询测试结果列表", description = "查找对应项目id下的测试结果列表")
    @Parameters(value = {
            @Parameter(name = "projectId", description = "项目id"),
            @Parameter(name = "page", description = "页码"),
            @Parameter(name = "pageSize", description = "页数据大小")
    })
    @GetMapping("/list")
    public RespModel<CommentPage<Results>> findByProjectId(@RequestParam(name = "projectId") int projectId,
                                                           @RequestParam(name = "page") int page,
                                                           @RequestParam(name = "pageSize") int pageSize) {
        Page<Results> pageable = new Page<>(page, pageSize);
        return new RespModel<>(
                RespEnum.SEARCH_OK,
                CommentPage.convertFrom(resultsService.findByProjectId(projectId, pageable))
        );
    }

    @WebAspect
    @Operation(summary = "删除测试结果", description = "删除对应的测试结果id以及测试结果详情")
    @Parameter(name = "id", description = "测试结果id")
    @DeleteMapping
    public RespModel<String> delete(@RequestParam(name = "id") int id) {
        if (resultsService.delete(id)) {
            return new RespModel<>(RespEnum.DELETE_OK);
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }

    @WebAspect
    @Operation(summary = "查询测试结果信息", description = "查询对应id的测试结果信息")
    @Parameter(name = "id", description = "测试结果id")
    @GetMapping
    public RespModel<Results> findById(@RequestParam(name = "id") int id) {
        return new RespModel<>(RespEnum.SEARCH_OK, resultsService.findById(id));
    }

    @WebAspect
    @Operation(summary = "清理测试结果", description = "按照指定天数前的测试结果")
    @GetMapping("/clean")
    public RespModel<String> clean(@RequestParam(name = "day") int day) {
        resultsService.clean(day);
        return new RespModel<>(0, "result.clean");
    }

    @WebAspect
    @Operation(summary = "统计测试结果", description = "统计测试结果")
    @GetMapping("/subResultCount")
    public RespModel<String> subResultCount(@RequestParam(name = "id") int id) {
        resultsService.subResultCount(id);
        return new RespModel<>(RespEnum.HANDLE_OK);
    }

    @WebAspect
    @Operation(summary = "查询测试结果用例状态", description = "查询对应id的测试结果用例状态")
    @Parameter(name = "id", description = "测试结果id")
    @GetMapping("/findCaseStatus")
    public RespModel<JSONArray> findCaseStatus(@RequestParam(name = "id") int id) {
        JSONArray result = resultsService.findCaseStatus(id);
        if (result == null) {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        } else {
            return new RespModel<>(RespEnum.SEARCH_OK, result);
        }
    }

    @WebAspect
    @Operation(summary = "查询报表", description = "查找前端首页报表信息")
    @Parameters(value = {
            @Parameter(name = "projectId", description = "项目id"),
            @Parameter(name = "startTime", description = "起始时间"),
            @Parameter(name = "endTime", description = "结束时间")
    })
    @GetMapping("/chart")
    public RespModel<JSONObject> chart(@RequestParam(name = "projectId") int projectId,
                                       @RequestParam(name = "startTime") String startTime,
                                       @RequestParam(name = "endTime") String endTime) {
        return new RespModel<>(RespEnum.SEARCH_OK, resultsService.chart(startTime, endTime, projectId));
    }

    @WebAspect
    @Operation(summary = "发送日报", description = "发送所有项目日报")
    @GetMapping("/sendDayReport")
    public RespModel<String> sendDayReport() {
        resultsService.sendDayReport();
        return new RespModel<>(RespEnum.HANDLE_OK);
    }

    @WebAspect
    @Operation(summary = "发送周报", description = "发送所有项目周报")
    @GetMapping("/sendWeekReport")
    public RespModel<String> sendWeekReport() {
        resultsService.sendWeekReport();
        return new RespModel<>(RespEnum.HANDLE_OK);
    }
}
