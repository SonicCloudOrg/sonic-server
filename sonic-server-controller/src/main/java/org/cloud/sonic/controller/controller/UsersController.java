package org.cloud.sonic.controller.controller;

import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.exception.SonicException;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.models.dto.UsersDTO;
import org.cloud.sonic.controller.models.http.ChangePwd;
import org.cloud.sonic.controller.models.http.UserInfo;
import org.cloud.sonic.controller.services.UsersService;
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
