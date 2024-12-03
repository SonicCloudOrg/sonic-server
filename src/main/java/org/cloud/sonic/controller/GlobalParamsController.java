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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.models.domain.GlobalParams;
import org.cloud.sonic.controller.models.dto.GlobalParamsDTO;
import org.cloud.sonic.controller.services.GlobalParamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "全局参数相关")
@RestController
@RequestMapping("/globalParams")
public class GlobalParamsController {

    @Autowired
    private GlobalParamsService globalParamsService;

    @WebAspect
    @Operation(summary = "更新全局参数", description = "新增或更新对应的全局参数")
    @PutMapping
    public RespModel<String> save(@Validated @RequestBody GlobalParamsDTO globalParamsDTO) {
        globalParamsService.save(globalParamsDTO.convertTo());
        return new RespModel<>(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @Operation(summary = "查找全局参数", description = "查找对应项目id的全局参数列表")
    @Parameter(name = "projectId", description = "项目id")
    @GetMapping("/list")
    public RespModel<List<GlobalParams>> findByProjectId(@RequestParam(name = "projectId") int projectId) {
        return new RespModel<>(RespEnum.SEARCH_OK, globalParamsService.findAll(projectId));
    }

    @WebAspect
    @Operation(summary = "删除全局参数", description = "删除对应id的全局参数")
    @Parameter(name = "id", description = "id")
    @DeleteMapping
    public RespModel<String> delete(@RequestParam(name = "id") int id) {
        if (globalParamsService.delete(id)) {
            return new RespModel<>(RespEnum.DELETE_OK);
        } else {
            return new RespModel<>(RespEnum.DELETE_FAIL);
        }
    }

    @WebAspect
    @Operation(summary = "查看全局参数信息", description = "查看对应id的全局参数")
    @Parameter(name = "id", description = "id")
    @GetMapping
    public RespModel<GlobalParams> findById(@RequestParam(name = "id") int id) {
        GlobalParams globalParams = globalParamsService.findById(id);
        if (globalParams != null) {
            return new RespModel<>(RespEnum.SEARCH_OK, globalParams);
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }
}
