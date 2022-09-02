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
import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.Elements;
import org.cloud.sonic.controller.models.dto.ElementsDTO;
import org.cloud.sonic.controller.models.dto.StepsDTO;
import org.cloud.sonic.controller.services.ElementsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "控件元素管理相关")
@RestController
@RequestMapping("/elements")
public class ElementsController {

    @Autowired
    private ElementsService elementsService;

    @WebAspect
    @ApiOperation(value = "查找控件元素列表1", notes = "查找对应项目id的控件元素列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "projectId", value = "项目id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "eleTypes[]", value = "类型(多个)", dataTypeClass = String.class),
            @ApiImplicitParam(name = "name", value = "控件名称", dataTypeClass = String.class),
            @ApiImplicitParam(name = "value", value = "控件值", dataTypeClass = String.class),
            @ApiImplicitParam(name = "type", value = "类型", dataTypeClass = String.class),
            @ApiImplicitParam(name = "moduleId", value = "模块ID", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "page", value = "页码", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageSize", value = "页数据大小", dataTypeClass = Integer.class)
    })
    @GetMapping("/list")
    public RespModel<CommentPage<ElementsDTO>> findAll(@RequestParam(name = "projectId") int projectId,
                                                    @RequestParam(name = "type", required = false) String type,
                                                    @RequestParam(name = "eleTypes[]", required = false) List<String> eleTypes,
                                                    @RequestParam(name = "name", required = false) String name,
                                                    @RequestParam(name = "value", required = false) String value,
                                                    @RequestParam(name = "moduleId", required = false) Integer moduleId,
                                                    @RequestParam(name = "page") int page,
                                                    @RequestParam(name = "pageSize") int pageSize) {
        Page<Elements> pageable = new Page<>(page, pageSize);
        return new RespModel<>(
                RespEnum.SEARCH_OK,
                        elementsService.findAll(projectId, type, eleTypes, name, value, moduleId, pageable)
        );
    }

    @WebAspect
    @ApiOperation(value = "查找控件元素详情", notes = "查找对应id的对应控件元素详细信息")
    @ApiImplicitParam(name = "id", value = "控件元素id", dataTypeClass = Integer.class)
    @GetMapping
    public RespModel<Elements> findById(@RequestParam(name = "id") int id) {
        Elements elements = elementsService.findById(id);
        if (elements != null) {
            return new RespModel<>(RespEnum.SEARCH_OK, elements);
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }

    @WebAspect
    @ApiOperation(value = "删除控件元素", notes = "删除对应id的控件元素，当控件元素存在于用例或id不存在时，删除失败")
    @ApiImplicitParam(name = "id", value = "元素id", dataTypeClass = Integer.class)
    @DeleteMapping
    public RespModel<String> delete(@RequestParam(name = "id") int id) {
        return elementsService.delete(id);
    }

    @WebAspect
    @ApiOperation(value = "删除控件元素前检验", notes = "返回引用控件的步骤")
    @ApiImplicitParam(name = "id", value = "元素id", dataTypeClass = Integer.class)
    @GetMapping("deleteCheck")
    public RespModel<List<StepsDTO>> deleteCheck(@RequestParam(name = "id") int id) {
        return new RespModel<>(RespEnum.SEARCH_OK, elementsService.findAllStepsByElementsId(id));
    }

    @WebAspect
    @ApiOperation(value = "更新控件元素", notes = "新增或更新控件元素信息，id为0时新增，否则为更新对应id的信息")
    @PutMapping
    public RespModel<String> save(@Validated @RequestBody ElementsDTO elementsDTO) {
        if (elementsService.save(elementsDTO.convertTo())) {
            return new RespModel<>(RespEnum.UPDATE_OK);
        } else {
            return new RespModel<>(RespEnum.UPDATE_FAIL);
        }
    }

    @WebAspect
    @ApiOperation(value = "复制控件元素", notes = "复制空间元素，按照元素ID")
    @ApiImplicitParam(name = "id", value = "元素id", dataTypeClass = Integer.class)
    @GetMapping("/copyEle")
    public RespModel<String> copy(@RequestParam(name = "id") int id) {
        return elementsService.copy(id);
    }
}
