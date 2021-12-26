package com.sonic.controller.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sonic.controller.models.base.TypeConverter;
import com.sonic.controller.models.domain.Projects;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@ApiModel("项目DTO 模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectsDTO implements Serializable, TypeConverter<ProjectsDTO, Projects> {

    @ApiModelProperty(value = "id", example = "1")
    Integer id;

    @NotBlank
    @ApiModelProperty(value = "项目名称", required = true, example = "test")
    String projectName;

    @ApiModelProperty(value = "项目描述", required = true, example = "Sonic项目描述")
    String projectDes;

    @ApiModelProperty(value = "机器人类型", required = true, example = "1")
    Integer robotType;

    @ApiModelProperty(value = "机器人token", required = true, example = "http://dingTalk.com?token=*****")
    String robotToken;

    @ApiModelProperty(value = "机器人加签密钥", required = true, example = "qwe***")
    String robotSecret;

    @ApiModelProperty(value = "项目图标", required = true, example = "http://img.jpg")
    String projectImg;

    @ApiModelProperty(value = "最后修改日期", example = "2021-08-15 11:23:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date editTime;
}
