package org.cloud.sonic.controller.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.Packages;

import java.io.Serializable;
import java.util.Date;

@Schema(name = "安装包模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageDTO implements Serializable, TypeConverter<PackageDTO, Packages> {

    @Schema(description = "id", example = "1")
    Integer id;

    @Positive
    @Schema(description = "项目id", required = true, example = "1")
    Integer projectId;

    @NotBlank
    @Schema(description = "安装包名称", required = true, example = "xxx.apk")
    String pkgName;

    @NotBlank
    @Schema(description = "平台", required = true, example = "android,ios")
    String platform;

    @NotBlank
    @Schema(description = "构建分支", required = true, example = "feat/login")
    String branch;

    @NotBlank
    @Schema(description = "下载地址", required = true, example = "http://xxx.com/xx.apk")
    String url;

    @NotBlank
    @Schema(description = "来源地址", required = true, example = "http://xxx.com/xx.apk")
    String buildUrl;

    @Schema(description = "创建时间", example = "2021-08-15 11:36:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date createTime;

    @Schema(description = "更新时间", example = "2021-08-15 11:36:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date updateTime;

}
