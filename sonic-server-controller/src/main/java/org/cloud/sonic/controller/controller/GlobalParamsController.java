/*
 *   sonic-server  Sonic Cloud Real Machine Platform.
 *   Copyright (C) 2022 SonicCloudOrg
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.cloud.sonic.controller.controller;

import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.models.domain.GlobalParams;
import org.cloud.sonic.controller.models.dto.GlobalParamsDTO;
import org.cloud.sonic.controller.services.GlobalParamsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "全局参数相关")
@RestController
@RequestMapping("/globalParams")
public class GlobalParamsController {

    @Autowired
    private GlobalParamsService globalParamsService;

    @WebAspect
    @ApiOperation(value = "更新全局参数", notes = "新增或更新对应的全局参数")
    @PutMapping
    public RespModel<String> save(@Validated @RequestBody GlobalParamsDTO globalParamsDTO) {
        globalParamsService.save(globalParamsDTO.convertTo());
        return new RespModel<>(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @ApiOperation(value = "查找全局参数", notes = "查找对应项目id的全局参数列表")
    @ApiImplicitParam(name = "projectId", value = "项目id", dataTypeClass = Integer.class)
    @GetMapping("/list")
    public RespModel<List<GlobalParams>> findByProjectId(@RequestParam(name = "projectId") int projectId) {
        return new RespModel<>(RespEnum.SEARCH_OK, globalParamsService.findAll(projectId));
    }

    @WebAspect
    @ApiOperation(value = "删除全局参数", notes = "删除对应id的全局参数")
    @ApiImplicitParam(name = "id", value = "id", dataTypeClass = Integer.class)
    @DeleteMapping
    public RespModel<String> delete(@RequestParam(name = "id") int id) {
        if (globalParamsService.delete(id)) {
            return new RespModel<>(RespEnum.DELETE_OK);
        } else {
            return new RespModel<>(RespEnum.DELETE_FAIL);
        }
    }

    @WebAspect
    @ApiOperation(value = "查看全局参数信息", notes = "查看对应id的全局参数")
    @ApiImplicitParam(name = "id", value = "id", dataTypeClass = Integer.class)
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
