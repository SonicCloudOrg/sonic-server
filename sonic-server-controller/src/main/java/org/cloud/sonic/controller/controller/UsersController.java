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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.config.WhiteUrl;
import org.cloud.sonic.common.exception.SonicException;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.common.tools.JWTTokenTool;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.Roles;
import org.cloud.sonic.controller.models.domain.Users;
import org.cloud.sonic.controller.models.dto.UsersDTO;
import org.cloud.sonic.controller.models.http.ChangePwd;
import org.cloud.sonic.controller.models.http.UserInfo;
import org.cloud.sonic.controller.services.RolesServices;
import org.cloud.sonic.controller.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author ZhouYiXun
 * @des
 * @date 2021/10/13 19:05
 */
@Tag(name = "用户体系相关")
@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UsersService usersService;

    @Autowired
    private RolesServices rolesServices;

    @Autowired
    private JWTTokenTool jwtTokenTool;

    @WebAspect
    @WhiteUrl
    @ApiOperation(value = "获取登录配置", notes = "获取登录信息配置")
    @GetMapping("/loginConfig")
    public RespModel<?> getLoginConfig() {
        return new RespModel(RespEnum.SEARCH_OK, usersService.getLoginConfig());
    }

    @WebAspect
    @ApiOperation(value = "生成用户对外Token", notes = "生成用户对外Token")
    @GetMapping("/generateToken")
    public RespModel<String> generateToken(@RequestParam(name = "day") int day, HttpServletRequest request) {
        String token = request.getHeader("SonicToken");
        if (token != null) {
            return new RespModel(RespEnum.HANDLE_OK, jwtTokenTool.getToken(jwtTokenTool.getUserName(token), day));
        } else {
            return new RespModel(RespEnum.UPDATE_FAIL);
        }
    }

    @WebAspect
    @WhiteUrl
    @ApiOperation(value = "登录", notes = "用户登录")
    @PostMapping("/login")
    public RespModel<String> login(@Validated @RequestBody UserInfo userInfo) {
        String token = usersService.login(userInfo);
        if (token != null) {
            return new RespModel<>(2000, "ok.login", token);
        } else {
            return new RespModel<>(2001, "fail.login");
        }
    }

    @WebAspect
    @WhiteUrl
    @ApiOperation(value = "注册", notes = "注册用户")
    @PostMapping("/register")
    public RespModel<String> register(@Validated @RequestBody UsersDTO users) throws SonicException {
        usersService.register(users.convertTo());
        return new RespModel<>(2000, "ok.register");
    }

    @WebAspect
    @WhiteUrl
    @ApiOperation(value = "获取用户信息", notes = "获取token的用户信息")
    @GetMapping
    public RespModel<?> getUserInfo(HttpServletRequest request) {
        if (request.getHeader("SonicToken") != null) {
            String token = request.getHeader("SonicToken");
            Users users = usersService.getUserInfo(token);
            UsersDTO usersDTO = users.convertTo();
            if (!Objects.isNull(users.getUserRole())) {
                Roles roles = rolesServices.findById(users.getUserRole());
                if (!Objects.isNull(roles)) {
                    usersDTO.setRole(roles.getId());
                    usersDTO.setRoleName(roles.getRoleName());
                }
            }
            return new RespModel<>(RespEnum.SEARCH_OK, usersDTO);
        } else {
            return new RespModel<>(RespEnum.UNAUTHORIZED);
        }
    }

    @WebAspect
    @WhiteUrl
    @ApiOperation(value = "修改密码", notes = "修改token的用户密码")
    @PutMapping
    public RespModel<String> changePwd(HttpServletRequest request, @Validated @RequestBody ChangePwd changePwd) {
        if (request.getHeader("SonicToken") != null) {
            String token = request.getHeader("SonicToken");
            return usersService.resetPwd(token, changePwd);
        } else {
            return new RespModel<>(RespEnum.UNAUTHORIZED);
        }
    }

    @WebAspect
    @ApiOperation(value = "查询所有用户信息", notes = "查询所有用户信息")
    @GetMapping("/list")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "page", value = "页码", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "userName", value = "角色名称", dataTypeClass = String.class),

    })
    public RespModel<CommentPage<UsersDTO>> listResources(@RequestParam(name = "page") int page
            , @RequestParam(name = "userName", required = false) String userName) {
        Page<Users> pageable = new Page<>(page, 20);

        return RespModel.result(RespEnum.SEARCH_OK, usersService.listUsers(pageable, userName));
    }

    @WebAspect
    @ApiOperation(value = "修改用户角色", notes = "修改用户角色")
    @PutMapping("/changeRole")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "roleId", value = "角色 id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "userId", value = "用户 id", dataTypeClass = Integer.class),

    })
    public RespModel<Boolean> changeRole(@RequestParam(name = "roleId") Integer roleId,
                                         @RequestParam(name = "userId") Integer userId) {

        return RespModel.result(RespEnum.UPDATE_OK, usersService.updateUserRole(userId, roleId));
    }
}
