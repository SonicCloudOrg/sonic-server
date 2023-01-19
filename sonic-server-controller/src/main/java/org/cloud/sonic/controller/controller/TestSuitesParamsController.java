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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.models.domain.GlobalParams;
import org.cloud.sonic.controller.models.domain.TestSuitesParams;
import org.cloud.sonic.controller.models.dto.GlobalParamsDTO;
import org.cloud.sonic.controller.models.dto.TestSuitesParamsDTO;
import org.cloud.sonic.controller.services.GlobalParamsService;
import org.cloud.sonic.controller.services.TestSuitesParamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "套件参数相关")
@RestController
@RequestMapping("/testSuitesParams")
public class TestSuitesParamsController {

    @Autowired
    private TestSuitesParamsService testSuitesParamsService;

    @WebAspect
    @ApiOperation(value = "更新套件参数", notes = "新增或更新对应的套件参数")
    @PutMapping
    public RespModel<String> save(@Validated @RequestBody TestSuitesParamsDTO testSuitesParamsDTO) {
        testSuitesParamsService.save(testSuitesParamsDTO.convertTo());
        return new RespModel<>(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @ApiOperation(value = "查找套件参数", notes = "查找对应套件id的套件参数列表")
    @ApiImplicitParam(name = "suiteId", value = "项目id", dataTypeClass = Integer.class)
    @GetMapping("/list")
    public RespModel<List<TestSuitesParams>> findBySuiteId(@RequestParam(name = "suiteId") int suiteId) {
        return new RespModel<>(RespEnum.SEARCH_OK, testSuitesParamsService.findAll(suiteId));
    }

    @WebAspect
    @ApiOperation(value = "删除套件参数", notes = "删除对应id的套件参数")
    @ApiImplicitParam(name = "id", value = "id", dataTypeClass = Integer.class)
    @DeleteMapping
    public RespModel<String> delete(@RequestParam(name = "id") int id) {
        if (testSuitesParamsService.delete(id)) {
            return new RespModel<>(RespEnum.DELETE_OK);
        } else {
            return new RespModel<>(RespEnum.DELETE_FAIL);
        }
    }

    @WebAspect
    @ApiOperation(value = "查看套件参数信息", notes = "查看对应id的套件参数")
    @ApiImplicitParam(name = "id", value = "id", dataTypeClass = Integer.class)
    @GetMapping
    public RespModel<TestSuitesParams> findById(@RequestParam(name = "id") int id) {
        TestSuitesParams testSuitesParams = testSuitesParamsService.findById(id);
        if (testSuitesParams != null) {
            return new RespModel<>(RespEnum.SEARCH_OK, testSuitesParams);
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }
}
