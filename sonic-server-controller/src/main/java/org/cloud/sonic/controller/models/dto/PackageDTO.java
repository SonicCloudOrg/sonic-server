package org.cloud.sonic.controller.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.Packages;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.Date;

@ApiModel("安装包模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageDTO implements Serializable, TypeConverter<PackageDTO, Packages> {

    @ApiModelProperty(value = "id", example = "1")
    Integer id;

    @Positive
    @ApiModelProperty(value = "项目id", required = true, example = "1")
    Integer projectId;

    @NotBlank
    @ApiModelProperty(value = "安装包名称", required = true, example = "xxx.apk")
    String pkgName;

    @NotBlank
    @ApiModelProperty(value = "平台", required = true, example = "android,ios")
    String platform;

    @NotBlank
    @ApiModelProperty(value = "构建分支", required = true, example = "feat/login")
    String branch;

    @NotBlank
    @ApiModelProperty(value = "下载地址", required = true, example = "http://xxx.com/xx.apk")
    String url;

    @NotBlank
    @ApiModelProperty(value = "来源地址", required = true, example = "http://xxx.com/xx.apk")
    String buildUrl;

    @ApiModelProperty(value = "创建时间", example = "2021-08-15 11:36:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date createTime;

    @ApiModelProperty(value = "更新时间", example = "2021-08-15 11:36:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date updateTime;

}
