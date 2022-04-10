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

import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.exception.SonicException;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.common.models.dto.UsersDTO;
import org.cloud.sonic.common.models.http.ChangePwd;
import org.cloud.sonic.common.models.http.UserInfo;
import org.cloud.sonic.common.services.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ZhouYiXun
 * @des
 * @date 2021/10/13 19:05
 */
@Api(tags = "用户体系相关")
@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UsersService usersService;

    @WebAspect
    @ApiOperation(value = "获取登录配置", notes = "获取登录信息配置")
    @GetMapping("/loginConfig")
    public RespModel<?> getLoginConfig() {
        return new RespModel(RespEnum.SEARCH_OK, usersService.getLoginConfig());
    }

    @WebAspect
    @ApiOperation(value = "登录", notes = "用户登录")
    @PostMapping("/login")
    public RespModel<String> login(@Validated @RequestBody UserInfo userInfo) {
        String token = usersService.login(userInfo);
        if (token != null) {
            return new RespModel<>(2000, "登录成功！", token);
        } else {
            return new RespModel<>(2001, "登录失败！");
        }
    }

    @WebAspect
    @ApiOperation(value = "注册", notes = "注册用户")
    @PostMapping("/register")
    public RespModel<String> register(@Validated @RequestBody UsersDTO users) throws SonicException {
        usersService.register(users.convertTo());
        return new RespModel<>(2000, "注册成功！");
    }

    @WebAspect
    @ApiOperation(value = "获取用户信息", notes = "获取token的用户信息")
    @GetMapping
    public RespModel<?> getUserInfo(HttpServletRequest request) {
        if (request.getHeader("SonicToken") != null) {
            String token = request.getHeader("SonicToken");
            return new RespModel<>(RespEnum.SEARCH_OK, usersService.getUserInfo(token).convertTo());
        } else {
            return new RespModel<>(RespEnum.UNAUTHORIZED);
        }
    }

    @WebAspect
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
}
