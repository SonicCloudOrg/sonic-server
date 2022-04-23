package org.cloud.sonic.controller.models.http;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel("登录请求模型")
public class UserInfo {
    @NotNull
    @ApiModelProperty(value = "账户名", required = true, example = "ZhouYiXun")
    private String userName;

    @NotNull
    @ApiModelProperty(value = "密码", required = true, example = "123456")
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
