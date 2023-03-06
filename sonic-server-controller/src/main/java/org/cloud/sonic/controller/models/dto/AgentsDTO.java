package org.cloud.sonic.controller.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.Agents;

import java.io.Serializable;

@Schema(name = "AgentDTO 端模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentsDTO implements Serializable, TypeConverter<AgentsDTO, Agents> {

    @Schema(description = "id")
    Integer id;

    @NotBlank
    @Schema(description = "Agent端名称")
    String name;

    @NotBlank
    @Schema(description = "Agent端系统类型")
    String systemType;

    @NotBlank
    @Schema(description = "Agent端版本号")
    String version;

    @NotBlank
    @Schema(description = "Agent端所在host")
    String host;

    @NotNull
    @Schema(description = "Agent端暴露web端口")
    int port;

    @Schema(description = "Agent端状态")
    int status;

    @Schema(description = "Agent端密钥")
    String secretKey;

    @Schema(description = "highTemp")
    Integer highTemp;

    @Schema(description = "highTempTime")
    Integer highTempTime;

    @Schema(description = "机器人类型")
    Integer robotType;

    @Schema(description = "机器人token")
    String robotToken;

    @Schema(description = "机器人加签密钥")
    String robotSecret;

    @Schema(description = "是否使用sonic hub")
    Integer hasHub;
}
