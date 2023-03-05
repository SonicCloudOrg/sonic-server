package org.cloud.sonic.controller.models.http;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;

@ApiModel("更新设备详情请求模型")
public class DeviceDetailChange implements Serializable {
    @Positive
    @ApiModelProperty(value = "设备id", required = true, example = "1")
    private int id;
    @NotNull
    @ApiModelProperty(value = "设备安装密码", required = true, example = "123456")
    private String password;
    @NotNull
    @ApiModelProperty(value = "设备备注", required = true, example = "123456")
    private String nickName;

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "DeviceDetailChange{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
