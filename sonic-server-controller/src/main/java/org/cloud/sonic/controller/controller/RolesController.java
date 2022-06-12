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
import org.cloud.sonic.controller.models.domain.Roles;
import org.cloud.sonic.controller.models.dto.RolesDTO;
import org.cloud.sonic.controller.services.RolesServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@Slf4j
@Api(tags = "角色相关")
@RestController
@RequestMapping("/roles")
public class RolesController {

    @Autowired
    private RolesServices rolesServices;

    @WebAspect
    @ApiOperation(value = "查询所有角色信息", notes = "查询所有角色信息")
    @GetMapping("/list")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "page", value = "页码", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "roleName", value = "角色名称", dataTypeClass = String.class),

    })
    public RespModel<CommentPage<RolesDTO>> listResources(@RequestParam(name = "page") int page
            , @RequestParam(name = "roleName", required = false)  String roleName) {
        Page<Roles> pageable = new Page<>(page, 20);

        return RespModel.result(RespEnum.SEARCH_OK, rolesServices.listRoles(pageable, roleName));
    }



    @WebAspect
    @ApiOperation(value = "编辑或新增角色", notes = "编辑或新增角色")
    @PutMapping("/edit")
    public RespModel<String> editResources(@RequestBody RolesDTO rolesDTO) {
        rolesServices.save(rolesDTO);
        return RespModel.result(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @ApiOperation(value = "删除角色", notes = "返回当前第一页角色")
    @ApiImplicitParam(name = "id", value = "角色id", dataTypeClass = Integer.class)
    @DeleteMapping("/delete")
    public RespModel<CommentPage<RolesDTO>> deleteCheck(@RequestParam(name = "id") int id) {

        rolesServices.delete(id);

        Page<Roles> pageable = new Page<>(1, 20);
        return  RespModel.result(RespEnum.SEARCH_OK, rolesServices.listRoles(pageable, null));
    }

    @WebAspect
    @ApiOperation(value = "编辑角色资源鉴权状态", notes = "编辑是否成功")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "roleId", value = "角色 id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "resId", value = "资源 id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "hasAuth", value = "是否有权限", dataTypeClass = Boolean.class)
    })
    @PutMapping("/update")
    public RespModel<String> editResourceRoles(@RequestParam(name = "roleId") int roleId,
                                               @RequestParam(name = "resId") int resId,
                                               @RequestParam(name = "hasAuth") boolean hasAuth) {

        rolesServices.editResourceRoles(roleId, resId, hasAuth);

        return RespModel.result(RespEnum.UPDATE_OK);
    }





}
