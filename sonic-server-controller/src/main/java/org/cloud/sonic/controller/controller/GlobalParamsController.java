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
import org.cloud.sonic.common.models.domain.GlobalParams;
import org.cloud.sonic.common.models.dto.GlobalParamsDTO;
import org.cloud.sonic.common.services.GlobalParamsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "公共参数相关")
@RestController
@RequestMapping("/globalParams")
public class GlobalParamsController {

    @Autowired
    private GlobalParamsService globalParamsService;

    @WebAspect
    @ApiOperation(value = "更新公共参数", notes = "新增或更新对应的公共参数")
    @PutMapping
    public RespModel<String> save(@Validated @RequestBody GlobalParamsDTO globalParamsDTO) {
        globalParamsService.save(globalParamsDTO.convertTo());
        return new RespModel<>(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @ApiOperation(value = "查找公共参数", notes = "查找对应项目id的公共参数列表")
    @ApiImplicitParam(name = "projectId", value = "项目id", dataTypeClass = Integer.class)
    @GetMapping("/list")
    public RespModel<List<GlobalParams>> findByProjectId(@RequestParam(name = "projectId") int projectId) {
        return new RespModel<>(RespEnum.SEARCH_OK, globalParamsService.findAll(projectId));
    }

    @WebAspect
    @ApiOperation(value = "删除公共参数", notes = "删除对应id的公共参数")
    @ApiImplicitParam(name = "id", value = "id", dataTypeClass = Integer.class)
    @DeleteMapping
    public RespModel<String> delete(@RequestParam(name = "id") int id) {
        if (globalParamsService.delete(id)) {
            return new RespModel<>(RespEnum.DELETE_OK);
        } else {
            return new RespModel<>(RespEnum.DELETE_ERROR);
        }
    }

    @WebAspect
    @ApiOperation(value = "查看公共参数信息", notes = "查看对应id的公共参数")
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
