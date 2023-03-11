package org.cloud.sonic.controller.models.http;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

@Schema(name = "更新设备图片请求模型")
public class UpdateDeviceImg implements Serializable {
    @Positive
    @Schema(description = "设备id", required = true, example = "1")
    private int id;
    @NotNull
    @Schema(description = "设备图片Url", required = true, example = "123456")
    private String imgUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "UpdateDeviceImg{" +
                "id=" + id +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
