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
            @ApiImplicitParam(name = "moduleIds", value = "模块ID", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "page", value = "页码", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageSize", value = "页数据大小", dataTypeClass = Integer.class)
    })
    @GetMapping("/list")
    public RespModel<CommentPage<ElementsDTO>> findAll(@RequestParam(name = "projectId") int projectId,
                                                    @RequestParam(name = "type", required = false) String type,
                                                    @RequestParam(name = "eleTypes[]", required = false) List<String> eleTypes,
                                                    @RequestParam(name = "name", required = false) String name,
                                                    @RequestParam(name = "value", required = false) String value,
                                                    @RequestParam(name = "moduleIds[]", required = false) List<Integer> moduleIds,
                                                    @RequestParam(name = "page") int page,
                                                    @RequestParam(name = "pageSize") int pageSize) {
        Page<Elements> pageable = new Page<>(page, pageSize);
        return new RespModel<>(
                RespEnum.SEARCH_OK,
                        elementsService.findAll(projectId, type, eleTypes, name, value, moduleIds, pageable)
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
    @GetMapping("/deleteCheck")
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
    @ApiOperation(value = "复制控件元素", notes = "复制控件元素，按照元素ID")
    @ApiImplicitParam(name = "id", value = "元素id", dataTypeClass = Integer.class)
    @GetMapping("/copyEle")
    public RespModel<String> copy(@RequestParam(name = "id") int id) {
        return elementsService.copy(id);
    }
}
