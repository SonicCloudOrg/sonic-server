package com.sonic.controller.models.http;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel("登录请求模型")
public class UserInfo {
    @NotNull
    @ApiModelProperty(value = "账户名", required = true, example = "ZhouYiXun")
    private String username;

    @NotNull
    @ApiModelProperty(value = "密码", required = true, example = "123456")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
