package com.sonic.controller.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sonic.common.config.WebAspect;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.controller.models.base.CommentPage;
import com.sonic.controller.models.domain.Elements;
import com.sonic.controller.models.dto.ElementsDTO;
import com.sonic.controller.services.ElementsService;
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
            @ApiImplicitParam(name = "type", value = "类型", dataTypeClass = String.class),
            @ApiImplicitParam(name = "page", value = "页码", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageSize", value = "页数据大小", dataTypeClass = Integer.class)
    })
    @GetMapping("/list")
    public RespModel<CommentPage<Elements>> findAll(@RequestParam(name = "projectId") int projectId,
                                                    @RequestParam(name = "type", required = false) String type,
                                                    @RequestParam(name = "eleTypes[]", required = false) List<String> eleTypes,
                                                    @RequestParam(name = "name", required = false) String name,
                                                    @RequestParam(name = "page") int page,
                                                    @RequestParam(name = "pageSize") int pageSize) {
        Page<Elements> pageable = new Page<>(page, pageSize);
        return new RespModel<>(
                RespEnum.SEARCH_OK,
                CommentPage.convertFrom(
                        elementsService.findAll(projectId, type, eleTypes, name, pageable)
                )
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
    @ApiOperation(value = "更新控件元素", notes = "新增或更新控件元素信息，id为0时新增，否则为更新对应id的信息")
    @PutMapping
    public RespModel<String> save(@Validated @RequestBody ElementsDTO elementsDTO) {
        if (elementsService.save(elementsDTO.convertTo())) {
            return new RespModel<>(RespEnum.UPDATE_OK);
        } else {
            return new RespModel<>(-1, "操作失败！请检查控件元素值是否过长！");
        }
    }
}
