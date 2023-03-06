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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.Scripts;
import org.cloud.sonic.controller.models.dto.ScriptsDTO;
import org.cloud.sonic.controller.services.ScriptsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "脚本模板管理相关")
@RestController
@RequestMapping("/scripts")
public class ScriptsController {
    @Autowired
    private ScriptsService scriptsService;

    @WebAspect
    @ApiOperation(value = "查找脚本模板列表", notes = "查找对应项目id的脚本列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "projectId", value = "项目id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "name", value = "名称", dataTypeClass = String.class),
            @ApiImplicitParam(name = "page", value = "页码", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageSize", value = "页数据大小", dataTypeClass = Integer.class)
    })
    @GetMapping("/list")
    public RespModel<CommentPage<Scripts>> findAll(@RequestParam(name = "projectId", required = false) Integer projectId,
                                                   @RequestParam(name = "name", required = false) String name,
                                                   @RequestParam(name = "page") int page,
                                                   @RequestParam(name = "pageSize") int pageSize) {
        Page<Scripts> pageable = new Page<>(page, pageSize);
        return new RespModel<>(
                RespEnum.SEARCH_OK,
                CommentPage.convertFrom(scriptsService
                        .findByProjectId(projectId, name, pageable))
        );
    }

    @WebAspect
    @ApiOperation(value = "查找脚本详情", notes = "查找对应id的对应脚本详细信息")
    @ApiImplicitParam(name = "id", value = "id", dataTypeClass = Integer.class)
    @GetMapping
    public RespModel<Scripts> findById(@RequestParam(name = "id") int id) {
        Scripts scripts = scriptsService.findById(id);
        if (scripts != null) {
            return new RespModel<>(RespEnum.SEARCH_OK, scripts);
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }

    @WebAspect
    @ApiOperation(value = "删除脚本模板", notes = "删除对应id")
    @ApiImplicitParam(name = "id", value = "id", dataTypeClass = Integer.class)
    @DeleteMapping
    public RespModel<String> delete(@RequestParam(name = "id") int id) {
        if (scriptsService.delete(id)) {
            return new RespModel<>(RespEnum.DELETE_OK);
        } else {
            return new RespModel<>(RespEnum.DELETE_FAIL);
        }
    }

    @WebAspect
    @ApiOperation(value = "更新脚本信息", notes = "新增或更新对应的脚本信息")
    @PutMapping
    public RespModel<String> save(@Validated @RequestBody ScriptsDTO scriptsDTO) {
        scriptsService.save(scriptsDTO.convertTo());
        return new RespModel<>(RespEnum.UPDATE_OK);
    }

}
