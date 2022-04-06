package org.cloud.sonic.common.models.http;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@ApiModel("更新设备图片请求模型")
public class UpdateDeviceImg implements Serializable {
    @Positive
    @ApiModelProperty(value = "设备id", required = true, example = "1")
    private int id;
    @NotNull
    @ApiModelProperty(value = "设备图片Url", required = true, example = "123456")
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
