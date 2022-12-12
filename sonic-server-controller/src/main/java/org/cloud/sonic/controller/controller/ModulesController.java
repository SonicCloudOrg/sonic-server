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

import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.Modules;
import org.cloud.sonic.controller.models.dto.ModulesDTO;
import org.cloud.sonic.controller.services.ModulesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "模块管理相关")
@RestController
@RequestMapping("/modules")
public class ModulesController {

    @Autowired
    private ModulesService modulesService;

    @WebAspect
    @ApiOperation(value = "更新模块信息", notes = "新增或更新对应的模块信息")
    @PutMapping
    public RespModel<String> save(@Validated @RequestBody ModulesDTO modules) {
        modulesService.save(modules.convertTo());
        return new RespModel<>(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @ApiOperation(value = "查找模块列表", notes = "查找对应项目id的模块列表")
    @ApiImplicitParam(name = "projectId", value = "项目id", dataTypeClass = Integer.class)
    @GetMapping("/list")
    public RespModel<List<ModulesDTO>> findByProjectId(@RequestParam(name = "projectId") int projectId) {
        return new RespModel<>(
                RespEnum.SEARCH_OK,
                modulesService.findByProjectId(projectId)
                        .stream().map(TypeConverter::convertTo).collect(Collectors.toList())
        );
    }

    @WebAspect
    @ApiOperation(value = "删除模块", notes = "删除对应id的模块")
    @ApiImplicitParam(name = "id", value = "模块id", dataTypeClass = Integer.class)
    @DeleteMapping
    public RespModel<String> delete(@RequestParam(name = "id") int id) {
        if (modulesService.delete(id)) {
            return new RespModel<>(RespEnum.DELETE_OK);
        } else {
            return new RespModel<>(RespEnum.DELETE_FAIL);
        }
    }

    @WebAspect
    @ApiOperation(value = "查看模块信息", notes = "查看对应id的模块信息")
    @ApiImplicitParam(name = "id", value = "模块id", dataTypeClass = Integer.class)
    @GetMapping
    public RespModel<ModulesDTO> findById(@RequestParam(name = "id") int id) {
        Modules modules = modulesService.findById(id);
        if (modules != null) {
            return new RespModel<>(RespEnum.SEARCH_OK, modules.convertTo());
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }
}
