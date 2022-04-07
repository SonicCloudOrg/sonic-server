/**
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

import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.common.models.base.TypeConverter;
import org.cloud.sonic.common.models.domain.Modules;
import org.cloud.sonic.common.models.dto.ModulesDTO;
import org.cloud.sonic.common.services.ModulesService;
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
            return new RespModel<>(RespEnum.DELETE_ERROR);
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
