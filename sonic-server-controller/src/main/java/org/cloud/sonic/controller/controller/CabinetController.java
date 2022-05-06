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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.common.models.base.TypeConverter;
import org.cloud.sonic.common.models.domain.Cabinet;
import org.cloud.sonic.common.models.dto.CabinetDTO;
import org.cloud.sonic.common.services.CabinetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZhouYiXun
 * @des
 * @date 2022/4/26 1:22
 */
@Api(tags = "机柜相关")
@RestController
@RequestMapping("/cabinet")
public class CabinetController {
    @Autowired
    private CabinetService cabinetService;

    @WebAspect
    @ApiOperation(value = "查询所有机柜", notes = "获取所有机柜以及详细信息")
    @GetMapping("/list")
    public RespModel<List<CabinetDTO>> findCabinets() {
        return new RespModel<>(
                RespEnum.SEARCH_OK,
                cabinetService.findCabinets().stream().map(TypeConverter::convertTo).collect(Collectors.toList())
        );
    }

    @WebAspect
    @ApiOperation(value = "修改机柜信息", notes = "修改对应机柜信息")
    @PutMapping
    public RespModel save(@RequestBody Cabinet cabinet) {
        cabinetService.saveCabinet(cabinet);
        return new RespModel<>(RespEnum.UPDATE_OK);
    }
}
