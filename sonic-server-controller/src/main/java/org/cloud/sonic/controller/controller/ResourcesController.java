package org.cloud.sonic.controller.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.Resources;
import org.cloud.sonic.controller.models.dto.ResourcesDTO;
import org.cloud.sonic.controller.models.params.ResourceParam;
import org.cloud.sonic.controller.services.ResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@Api(tags = "请求路径资源")
@RestController
@RequestMapping("/resources")
public class ResourcesController {

    @Autowired
    private ResourcesService resourcesService;

    @WebAspect
    @ApiOperation(value = "查询所有资源连接", notes = "查询所有资源连接")
    @GetMapping("/list")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "page", value = "页码", dataTypeClass = Integer.class)
    })
    public RespModel<CommentPage<ResourcesDTO>> listResources(@RequestParam(name = "page") int page,
                                                              @RequestParam(name = "path", required = false) String path) {
        Page<Resources> pageable = new Page<>(page, 20);

        return RespModel.result(RespEnum.SEARCH_OK, resourcesService.listResource(pageable, path, false));
    }

    @WebAspect
    @ApiOperation(value = "刷新请求资源列表", notes = "查询所有资源连接")
    @PostMapping("/refresh")
    public RespModel<CommentPage<ResourcesDTO>> refreshResources() {
        resourcesService.init();
        return listResources(1, null);
    }


    @WebAspect
    @ApiOperation(value = "编辑资源鉴权状态", notes = "编辑资源鉴权状态")
    @PutMapping("/edit")
    public RespModel<String> editResources(@RequestBody ResourceParam resourceParam) {

        resourcesService.updateResourceAuth(resourceParam.getId(), resourceParam.getNeedAuth());

        return RespModel.result(RespEnum.UPDATE_OK);
    }


    @WebAspect
    @ApiOperation(value = "查询当前角色下资源鉴权转态", notes = "查询当前角色下资源鉴权转态")
    @GetMapping("/roleResource")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "roleId", value = "角色 id", dataTypeClass = Integer.class)
    })
    public RespModel<List<ResourcesDTO>> listRoleResource(@RequestParam(name = "roleId") Integer roleId) {

        return RespModel.result(RespEnum.SEARCH_OK, resourcesService.listRoleResource(roleId));
    }

}
