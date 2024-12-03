package org.cloud.sonic.controller.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "请求路径资源")
@RestController
@RequestMapping("/resources")
public class ResourcesController {

    @Autowired
    private ResourcesService resourcesService;

    @WebAspect
    @Operation(summary = "查询所有资源连接", description = "查询所有资源连接")
    @GetMapping("/list")
    @Parameters(value = {
            @Parameter(name = "page", description = "页码"),
            @Parameter(name = "pageSize", description = "每页请求数量")
    })
    public RespModel<CommentPage<ResourcesDTO>> listResources(@RequestParam(name = "page") int page,
                                                              @RequestParam(name = "pageSize") int pageSize,
                                                              @RequestParam(name = "path", required = false) String path) {
        Page<Resources> pageable = new Page<>(page, pageSize);

        return RespModel.result(RespEnum.SEARCH_OK, resourcesService.listResource(pageable, path, false));
    }

    @WebAspect
    @Operation(summary = "刷新请求资源列表", description = "查询所有资源连接")
    @PostMapping("/refresh")
    public RespModel<CommentPage<ResourcesDTO>> refreshResources() {
        resourcesService.init();
        return listResources(1, 20, null);
    }


    @WebAspect
    @Operation(summary = "编辑资源鉴权状态", description = "编辑资源鉴权状态")
    @PutMapping("/edit")
    public RespModel<String> editResources(@RequestBody ResourceParam resourceParam) {

        resourcesService.updateResourceAuth(resourceParam.getId(), resourceParam.getNeedAuth());

        return RespModel.result(RespEnum.UPDATE_OK);
    }


    @WebAspect
    @Operation(summary = "查询当前角色下资源鉴权转态", description = "查询当前角色下资源鉴权转态")
    @GetMapping("/roleResource")
    @Parameters(value = {
            @Parameter(name = "roleId", description = "角色 id")
    })
    public RespModel<List<ResourcesDTO>> listRoleResource(@RequestParam(name = "roleId") Integer roleId) {

        return RespModel.result(RespEnum.SEARCH_OK, resourcesService.listRoleResource(roleId));
    }

}
