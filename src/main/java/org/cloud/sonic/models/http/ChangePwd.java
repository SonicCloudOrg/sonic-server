package org.cloud.sonic.controller.models.http;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * @author ZhouYiXun
 * @des
 * @date 2021/10/13 18:45
 */
@Schema(name = "更改密码请求模型")
public class ChangePwd implements Serializable {
    @NotNull
    @Schema(description = "旧密码", required = true, example = "123456")
    private String oldPwd;

    @NotNull
    @Schema(description = "新密码", required = true, example = "123456")
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
