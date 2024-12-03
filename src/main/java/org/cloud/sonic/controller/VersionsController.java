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
import org.cloud.sonic.controller.models.domain.Versions;
import org.cloud.sonic.controller.models.dto.VersionsDTO;
import org.cloud.sonic.controller.services.VersionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "版本迭代相关")
@RestController
@RequestMapping("/versions")
public class VersionsController {
    @Autowired
    private VersionsService versionsService;

    @WebAspect
    @Operation(summary = "更新版本迭代", description = "新增或更改版本迭代信息")
    @PutMapping
    public RespModel<String> save(@Validated @RequestBody VersionsDTO versionsDTO) {
        versionsService.save(versionsDTO.convertTo());
        return new RespModel<>(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @Operation(summary = "查询版本迭代列表", description = "用于查询对应项目id下的版本迭代列表")
    @Parameter(name = "projectId", description = "项目id")
    @GetMapping("/list")
    public RespModel<List<Versions>> findByProjectId(@RequestParam(name = "projectId") int projectId) {
        return new RespModel<>(RespEnum.SEARCH_OK, versionsService.findByProjectId(projectId));
    }

    @WebAspect
    @Operation(summary = "删除版本迭代", description = "删除指定id的版本迭代")
    @Parameter(name = "id", description = "版本迭代id")
    @DeleteMapping
    public RespModel<String> delete(@RequestParam(name = "id") int id) {
        if (versionsService.delete(id)) {
            return new RespModel<>(RespEnum.DELETE_OK);
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }

    @WebAspect
    @Operation(summary = "查询版本迭代信息", description = "查询指定id的版本迭代的详细信息")
    @Parameter(name = "id", description = "版本迭代id")
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
