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
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.ResultDetail;
import org.cloud.sonic.controller.services.ResultDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des
 * @date 2021/8/29 16:59
 */
@Tag(name = "测试结果详情相关")
@RestController
@RequestMapping("/resultDetail")
public class ResultDetailController {

    @Autowired
    private ResultDetailService resultDetailService;

    @WebAspect
    @Operation(summary = "保存测试结果", description = "保存测试结果")
    @PostMapping
    public RespModel<String> save(@RequestBody JSONObject jsonObject) {
        resultDetailService.saveByTransport(jsonObject);
        return new RespModel<>(RespEnum.HANDLE_OK);
    }

    @WebAspect
    @Operation(summary = "查找测试结果详情", description = "查找对应测试结果详情")
    @Parameters(value = {
            @Parameter(name = "caseId", description = "测试用例id"),
            @Parameter(name = "resultId", description = "测试结果id"),
            @Parameter(name = "deviceId", description = "设备id"),
            @Parameter(name = "type", description = "类型"),
            @Parameter(name = "page", description = "页码")
    })
    @GetMapping("/list")
    public RespModel<CommentPage<ResultDetail>> findAll(@RequestParam(name = "caseId") int caseId,
                                                        @RequestParam(name = "resultId") int resultId,
                                                        @RequestParam(name = "deviceId") int deviceId,
                                                        @RequestParam(name = "type") String type,
                                                        @RequestParam(name = "page") int page) {
        Page<ResultDetail> pageable = new Page<>(page, 20);
        return new RespModel<>(RespEnum.SEARCH_OK,
                CommentPage.convertFrom(
                        resultDetailService.findAll(resultId, caseId, type, deviceId, pageable))
        );
    }

    @WebAspect
    @Operation(summary = "查找测试结果详情2", description = "查找对应测试结果详情")
    @Parameters(value = {
            @Parameter(name = "caseId", description = "测试用例id"),
            @Parameter(name = "resultId", description = "测试结果id"),
            @Parameter(name = "deviceId", description = "设备id"),
            @Parameter(name = "type", description = "类型"),
    })
    @GetMapping("/listAll")
    public RespModel<List<ResultDetail>> findAll(@RequestParam(name = "caseId") int caseId,
                                                 @RequestParam(name = "resultId") int resultId,
                                                 @RequestParam(name = "deviceId") int deviceId,
                                                 @RequestParam(name = "type") String type) {
        return new RespModel<>(RespEnum.SEARCH_OK,
                resultDetailService.findAll(resultId, caseId, type, deviceId));
    }
}
