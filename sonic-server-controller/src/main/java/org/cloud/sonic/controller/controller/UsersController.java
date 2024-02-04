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
    @Operation(summary = "获取登录配置", description = "获取登录信息配置")
    @GetMapping("/loginConfig")
    public RespModel<?> getLoginConfig() {
        return new RespModel(RespEnum.SEARCH_OK, usersService.getLoginConfig());
    }

    @WebAspect
    @Operation(summary = "生成用户对外Token", description = "生成用户对外Token")
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
    @Operation(summary = "登录", description = "用户登录")
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
    @Operation(summary = "注册", description = "注册用户")
    @PostMapping("/register")
    public RespModel<String> register(@Validated @RequestBody UsersDTO users) throws SonicException {
        usersService.register(users.convertTo());
        return new RespModel<>(2000, "ok.register");
    }

    @WebAspect
    @WhiteUrl
    @Operation(summary = "获取用户信息", description = "获取token的用户信息")
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
    @Operation(summary = "修改密码", description = "修改token的用户密码")
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
    @Operation(summary = "查询所有用户信息", description = "查询所有用户信息")
    @GetMapping("/list")
    @Parameters(value = {
            @Parameter(name = "page", description = "页码"),
            @Parameter(name = "pageSize", description = "每页请求数量"),
            @Parameter(name = "userName", description = "角色名称"),

    })
    public RespModel<CommentPage<UsersDTO>> listResources(@RequestParam(name = "page") int page,
                                                          @RequestParam(name = "pageSize", required = false, defaultValue = "20") int pageSize,
                                                          @RequestParam(name = "userName", required = false) String userName) {
        Page<Users> pageable = new Page<>(page, pageSize);

        return RespModel.result(RespEnum.SEARCH_OK, usersService.listUsers(pageable, userName));
    }

    @WebAspect
    @Operation(summary = "修改用户角色", description = "修改用户角色")
    @PutMapping("/changeRole")
    @Parameters(value = {
            @Parameter(name = "roleId", description = "角色 id"),
            @Parameter(name = "userId", description = "用户 id"),

    })
    public RespModel<Boolean> changeRole(@RequestParam(name = "roleId") Integer roleId,
                                         @RequestParam(name = "userId") Integer userId) {

        return RespModel.result(RespEnum.UPDATE_OK, usersService.updateUserRole(userId, roleId));
    }
}
