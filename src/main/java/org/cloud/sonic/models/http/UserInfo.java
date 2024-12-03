package org.cloud.sonic.models.http;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@Schema(name = "登录请求模型")
public class UserInfo implements Serializable {
    @NotNull
    @Schema(description = "账户名", required = true, example = "ZhouYiXun")
    private String userName;

    @NotNull
    @Schema(description = "密码", required = true, example = "123456")
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
