/*
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
import org.cloud.sonic.common.models.domain.Versions;
import org.cloud.sonic.common.models.dto.VersionsDTO;
import org.cloud.sonic.common.services.VersionsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "版本迭代相关")
@RestController
@RequestMapping("/versions")
public class VersionsController {
    @Autowired
    private VersionsService versionsService;

    @WebAspect
    @ApiOperation(value = "更新版本迭代", notes = "新增或更改版本迭代信息")
    @PutMapping
    public RespModel<String> save(@Validated @RequestBody VersionsDTO versionsDTO) {
        versionsService.save(versionsDTO.convertTo());
        return new RespModel<>(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @ApiOperation(value = "查询版本迭代列表", notes = "用于查询对应项目id下的版本迭代列表")
    @ApiImplicitParam(name = "projectId", value = "项目id", dataTypeClass = Integer.class)
    @GetMapping("/list")
    public RespModel<List<Versions>> findByProjectId(@RequestParam(name = "projectId") int projectId) {
        return new RespModel<>(RespEnum.SEARCH_OK, versionsService.findByProjectId(projectId));
    }

    @WebAspect
    @ApiOperation(value = "删除版本迭代", notes = "删除指定id的版本迭代")
    @ApiImplicitParam(name = "id", value = "版本迭代id", dataTypeClass = Integer.class)
    @DeleteMapping
    public RespModel<String> delete(@RequestParam(name = "id") int id) {
        if (versionsService.delete(id)) {
            return new RespModel<>(RespEnum.DELETE_OK);
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }

    @WebAspect
    @ApiOperation(value = "查询版本迭代信息", notes = "查询指定id的版本迭代的详细信息")
    @ApiImplicitParam(name = "id", value = "版本迭代id", dataTypeClass = Integer.class)
    @GetMapping
    public RespModel<Versions> findById(@RequestParam(name = "id") int id) {
        Versions versions = versionsService.findById(id);
        if (versions != null) {
            return new RespModel<>(RespEnum.SEARCH_OK, versions);
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }
}
