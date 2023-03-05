package org.cloud.sonic.controller.models.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.Agents;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel("AgentDTO 端模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentsDTO implements Serializable, TypeConverter<AgentsDTO, Agents> {

    @ApiModelProperty(value = "id", example = "1")
    Integer id;

    @NotBlank
    @ApiModelProperty(value = "Agent端名称", example = "本地Agent")
    String name;

    @NotBlank
    @ApiModelProperty(value = "Agent端系统类型", example = "Windows 10")
    String systemType;

    @NotBlank
    @ApiModelProperty(value = "Agent端版本号", example = "1.0.0")
    String version;

    @NotBlank
    @ApiModelProperty(value = "Agent端所在host", example = "127.0.0.1")
    String host;

    @NotNull
    @ApiModelProperty(value = "Agent端暴露web端口", example = "1234")
    int port;

    @ApiModelProperty(value = "Agent端状态", example = "1")
    int status;

    @ApiModelProperty(value = "Agent端密钥", example = "qwe")
    String secretKey;

    @ApiModelProperty(value = "highTemp", example = "1")
    Integer highTemp;

    @ApiModelProperty(value = "highTempTime", example = "1")
    Integer highTempTime;

    @ApiModelProperty(value = "机器人类型", required = true, example = "1")
    Integer robotType;

    @ApiModelProperty(value = "机器人token", required = true, example = "http://dingTalk.com?token=*****")
    String robotToken;

    @ApiModelProperty(value = "机器人加签密钥", required = true, example = "qwe***")
    String robotSecret;

    @ApiModelProperty(value = "是否使用sonic hub", example = "1")
    Integer hasHub;
}
