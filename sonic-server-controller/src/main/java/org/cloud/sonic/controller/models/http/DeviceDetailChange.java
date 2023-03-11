package org.cloud.sonic.controller.models.http;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

@Schema(name = "更新设备详情请求模型")
public class DeviceDetailChange implements Serializable {
    @Positive
    @Schema(description = "设备id", required = true, example = "1")
    private int id;
    @NotNull
    @Schema(description = "设备安装密码", required = true, example = "123456")
    private String password;
    @NotNull
    @Schema(description = "设备备注", required = true, example = "123456")
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
