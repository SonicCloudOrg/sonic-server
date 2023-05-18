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
import io.swagger.v3.oas.annotations.tags.Tag;
import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.Packages;
import org.cloud.sonic.controller.models.dto.PackageDTO;
import org.cloud.sonic.controller.services.PackagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author yaming116, Eason
 * @des
 * @date 2022/5/26 1:22
 */
@Tag(name = "安装包管理")
@RestController
@RequestMapping("/packages")
public class PackageController {

    @Autowired
    private PackagesService packagesService;

    @WebAspect
    @Operation(summary = "添加安装包信息", description = "添加安装包信息")
    @PutMapping
    public RespModel<String> save(@Validated @RequestBody PackageDTO pkg) {
        packagesService.save(pkg.convertTo());
        return new RespModel<>(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @Operation(summary = "查找所有安装包", description = "查找所有安装包")
    @GetMapping("/list")
    public RespModel<CommentPage<PackageDTO>> findAll(@RequestParam(name = "projectId") int projectId,
                                                      @RequestParam(name = "branch", required = false) String branch,
                                                      @RequestParam(name = "platform", required = false) String platform,
                                                      @RequestParam(name = "packageName", required = false) String packageName,
                                                      @RequestParam(name = "page") int page,
                                                      @RequestParam(name = "pageSize") int pageSize) {

        Page<Packages> pageable = new Page<>(page, pageSize);

        return new RespModel<>(RespEnum.SEARCH_OK, packagesService.findByProjectId(projectId, branch, platform,
                packageName, pageable));
    }
}
