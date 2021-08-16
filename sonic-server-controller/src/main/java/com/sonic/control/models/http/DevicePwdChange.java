package com.sonic.control.models.http;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@ApiModel("更新设备密码请求模型")
public class DevicePwdChange {
    @Positive
    @ApiModelProperty(value = "设备id", required = true, example = "1")
    private int id;
    @NotNull
    @ApiModelProperty(value = "设备安装密码", required = true, example = "123456")
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "DevicePwdChange{" +
                "id=" + id +
                ", password='" + password + '\'' +
                '}';
    }
}
