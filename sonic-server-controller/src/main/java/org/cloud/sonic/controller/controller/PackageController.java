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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yaming116, Eason
 * @des
 * @date 2022/5/26 1:22
 */
@Api(tags = "安装包管理")
@RestController
@RequestMapping("/packages")
public class PackageController {

    @Autowired
    private PackagesService packagesService;

    @WebAspect
    @ApiOperation(value = "添加安装包信息", notes = "添加安装包信息")
    @PutMapping
    public RespModel<String> save(@Validated @RequestBody PackageDTO pkg) {
        packagesService.save(pkg.convertTo());
        return new RespModel<>(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @ApiOperation(value = "查找所有安装包", notes = "查找所有安装包")
    @GetMapping("/list")
    public RespModel<CommentPage<PackageDTO>> findAll(@RequestParam(name = "projectId") int projectId,
                                                      @RequestParam(name = "branch", required = false) String branch,
                                                      @RequestParam(name = "platform", required = false) String platform,
                                                      @RequestParam(name = "page") int page,
                                                      @RequestParam(name = "pageSize") int pageSize) {

        Page<Packages> pageable = new Page<>(page, pageSize);

        return new RespModel<>(RespEnum.SEARCH_OK, packagesService.findByProjectId(projectId, branch, platform, pageable));
    }
}
