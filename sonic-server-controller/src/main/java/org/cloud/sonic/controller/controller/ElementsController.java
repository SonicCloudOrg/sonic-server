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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.Elements;
import org.cloud.sonic.controller.models.dto.ElementsDTO;
import org.cloud.sonic.controller.models.dto.StepsDTO;
import org.cloud.sonic.controller.services.ElementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "控件元素管理相关")
@RestController
@RequestMapping("/elements")
public class ElementsController {

    @Autowired
    private ElementsService elementsService;

    @WebAspect
    @Operation(summary = "查找控件元素列表1", description = "查找对应项目id的控件元素列表")
    @Parameters(value = {
            @Parameter(name = "projectId", description = "项目id"),
            @Parameter(name = "eleTypes[]", description = "类型(多个)"),
            @Parameter(name = "name", description = "控件名称"),
            @Parameter(name = "value", description = "控件值"),
            @Parameter(name = "type", description = "类型"),
            @Parameter(name = "moduleIds", description = "模块ID"),
            @Parameter(name = "page", description = "页码"),
            @Parameter(name = "pageSize", description = "页数据大小")
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
    @Operation(summary = "查找控件元素详情", description = "查找对应id的对应控件元素详细信息")
    @Parameter(name = "id", description = "控件元素id")
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
    @Operation(summary = "删除控件元素", description = "删除对应id的控件元素，当控件元素存在于用例或id不存在时，删除失败")
    @Parameter(name = "id", description = "元素id")
    @DeleteMapping
    public RespModel<String> delete(@RequestParam(name = "id") int id) {
        return elementsService.delete(id);
    }

    @WebAspect
    @Operation(summary = "删除控件元素前检验", description = "返回引用控件的步骤")
    @Parameter(name = "id", description = "元素id")
    @GetMapping("/deleteCheck")
    public RespModel<List<StepsDTO>> deleteCheck(@RequestParam(name = "id") int id) {
        return new RespModel<>(RespEnum.SEARCH_OK, elementsService.findAllStepsByElementsId(id));
    }

    @WebAspect
    @Operation(summary = "更新控件元素", description = "新增或更新控件元素信息，id为0时新增，否则为更新对应id的信息")
    @PutMapping
    public RespModel<String> save(@Validated @RequestBody ElementsDTO elementsDTO) {
        if (elementsService.save(elementsDTO.convertTo())) {
            return new RespModel<>(RespEnum.UPDATE_OK);
        } else {
            return new RespModel<>(RespEnum.UPDATE_FAIL);
        }
    }

    @WebAspect
    @Operation(summary = "复制控件元素", description = "复制控件元素，按照元素ID")
    @Parameter(name = "id", description = "元素id")
    @GetMapping("/copyEle")
    public RespModel<String> copy(@RequestParam(name = "id") int id) {
        return elementsService.copy(id);
    }
}
