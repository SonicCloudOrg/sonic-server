package com.sonic.controller.models.http;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * @author ZhouYiXun
 * @des
 * @date 2021/10/13 18:45
 */
public class ChangePwd {
    @NotNull
    @ApiModelProperty(value = "旧密码", required = true, example = "123456")
    private String oldPwd;

    @NotNull
    @ApiModelProperty(value = "新密码", required = true, example = "123456")
    private String newPwd;

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
}
