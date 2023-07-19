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
import org.cloud.sonic.common.config.WhiteUrl;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.AlertRobots;
import org.cloud.sonic.controller.models.dto.AlertRobotsDTO;
import org.cloud.sonic.controller.services.AlertRobotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "告警通知机器人相关")
@RestController
@RequestMapping("/alertRobotsAdmin")
public class AlertRobotsAdminController {

    @Autowired
    private AlertRobotsService alertRobotsService;

    @WebAspect
    @Operation(summary = "更新机器人参数", description = "新增或更新对应的机器人")
    @PutMapping
    public RespModel<String> save(@Validated @RequestBody AlertRobotsDTO alertRobotsDTO) {
        alertRobotsService.saveOrUpdate(alertRobotsDTO.convertTo());
        return new RespModel<>(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @Operation(summary = "查找机器人参数", description = "查找所有机器人参数列表")
    @GetMapping("/list")
    @Parameters(value = {
            @Parameter(name = "scene", allowEmptyValue = true, description = "使用场景"),
            @Parameter(name = "page", description = "页码"),
            @Parameter(name = "pageSize", description = "页尺寸")
    })
    public RespModel<CommentPage<AlertRobots>> listAll(
            @RequestParam(name = "scene", required = false) String scene,
            @RequestParam(name = "page") int page,
            @RequestParam(name = "pageSize", defaultValue = "20") int pageSize
    ) {
        return new RespModel<>(RespEnum.SEARCH_OK, alertRobotsService.findRobots(new Page<>(page, pageSize), null, scene));
    }

    @WebAspect
    @Operation(summary = "查找机器人参数", description = "查找所有机器人参数列表")
    @GetMapping("/listAll")
    @Parameters(value = {
            @Parameter(name = "scene", allowEmptyValue = true, description = "使用场景")
    })
    public RespModel<List<AlertRobots>> listAll(
            @RequestParam(name = "scene", required = false) String scene
    ) {
        return new RespModel<>(RespEnum.SEARCH_OK, alertRobotsService.findAllRobots(null, scene));
    }

    @WebAspect
    @Operation(summary = "删除机器人参数", description = "删除对应id的机器人参数")
    @Parameter(name = "id", description = "id")
    @DeleteMapping
    public RespModel<String> delete(@RequestParam(name = "id") int id) {
        if (alertRobotsService.removeById(id)) {
            return new RespModel<>(RespEnum.DELETE_OK);
        } else {
            return new RespModel<>(RespEnum.DELETE_FAIL);
        }
    }

    @WebAspect
    @Operation(summary = "获取机器人对类型机器人在相应使用场景下的默认模板", description = "获取机器人对类型机器人在相应使用场景下的默认模板")
    @Parameter(name = "type", description = "type")
    @Parameter(name = "scene", description = "scene")
    @GetMapping("/findDefaultTemplate")
    @WhiteUrl
    public RespModel<String> getDefaultNoticeTemplate(@RequestParam(name = "type") int type, @RequestParam(name = "scene") String scene) {
        return new RespModel<>(RespEnum.SEARCH_OK, alertRobotsService.getDefaultNoticeTemplate(type, scene));
    }
}
