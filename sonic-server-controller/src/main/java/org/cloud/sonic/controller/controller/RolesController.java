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
import org.cloud.sonic.controller.models.domain.Roles;
import org.cloud.sonic.controller.models.dto.RolesDTO;
import org.cloud.sonic.controller.services.RolesServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "角色相关")
@RestController
@RequestMapping("/roles")
public class RolesController {

    @Autowired
    private RolesServices rolesServices;

    @WebAspect
    @Operation(summary = "查询所有角色信息", description = "查询所有角色信息")
    @GetMapping("/list")
    @Parameters(value = {
            @Parameter(name = "page", description = "页码"),
            @Parameter(name = "pageSize", description = "每页请求数量"),
            @Parameter(name = "isAll", description = "是否全部"),
            @Parameter(name = "roleName", description = "角色名称"),

    })
    public RespModel<CommentPage<RolesDTO>> listResources(@RequestParam(name = "page") int page,
                                                          @RequestParam(name = "pageSize", required = false, defaultValue = "20") int pageSize,
                                                          @RequestParam(name = "isAll", required = false) boolean isAll,
                                                          @RequestParam(name = "roleName", required = false) String roleName) {
        Page<Roles> pageable = new Page<>(page, pageSize);
        if (isAll) {
            pageable.setSize(1000L);
        }

        return RespModel.result(RespEnum.SEARCH_OK, rolesServices.listRoles(pageable, roleName));
    }


    @WebAspect
    @Operation(summary = "编辑或新增角色", description = "编辑或新增角色")
    @PutMapping("/edit")
    public RespModel<String> editResources(@RequestBody RolesDTO rolesDTO) {
        rolesServices.save(rolesDTO);
        return RespModel.result(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @Operation(summary = "删除角色", description = "返回当前第一页角色")
    @Parameter(name = "id", description = "角色id")
    @DeleteMapping("/delete")
    public RespModel<CommentPage<RolesDTO>> deleteCheck(@RequestParam(name = "id") int id) {

        rolesServices.delete(id);

        Page<Roles> pageable = new Page<>(1, 20);
        return RespModel.result(RespEnum.SEARCH_OK, rolesServices.listRoles(pageable, null));
    }

    @WebAspect
    @Operation(summary = "编辑角色资源鉴权状态", description = "编辑是否成功")
    @Parameters(value = {
            @Parameter(name = "roleId", description = "角色 id"),
            @Parameter(name = "resId", description = "资源 id"),
            @Parameter(name = "hasAuth", description = "是否有权限")
    })
    @PutMapping("/update")
    public RespModel<String> editResourceRoles(@RequestParam(name = "roleId") int roleId,
                                               @RequestParam(name = "resId") int resId,
                                               @RequestParam(name = "hasAuth") boolean hasAuth) {

        rolesServices.editResourceRoles(roleId, resId, hasAuth);

        return RespModel.result(RespEnum.UPDATE_OK);
    }


}
