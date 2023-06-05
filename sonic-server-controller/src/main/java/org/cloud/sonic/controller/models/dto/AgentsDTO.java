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

    @Schema(description = "id", example = "1")
    Integer id;

    @NotBlank
    @Schema(description = "Agent端名称", example = "name")
    String name;

    @NotBlank
    @Schema(description = "Agent端系统类型", example = "Windows 10")
    String systemType;

    @NotBlank
    @Schema(description = "Agent端版本号", example = "v2.4.0")
    String version;

    @NotBlank
    @Schema(description = "Agent端所在host", example = "192.168.1.1")
    String host;

    @NotNull
    @Schema(description = "Agent端暴露web端口", example = "7777")
    int port;

    @Schema(description = "Agent端状态", example = "ONLINE")
    int status;

    @Schema(description = "Agent端密钥", example = "key")
    String secretKey;

    @Schema(description = "highTemp", example = "45")
    Integer highTemp;

    @Schema(description = "highTempTime", example = "10")
    Integer highTempTime;

    /**
     * @see org.cloud.sonic.controller.config.mybatis.RobotConfigMigrate
     * @deprecated
     */
    @Deprecated(forRemoval = true)
    @Schema(description = "机器人类型", example = "1")
    Integer robotType;

    /**
     * @see org.cloud.sonic.controller.config.mybatis.RobotConfigMigrate
     * @deprecated
     */
    @Deprecated(forRemoval = true)
    @Schema(description = "机器人token", example = "token")
    String robotToken;

    /**
     * @see org.cloud.sonic.controller.config.mybatis.RobotConfigMigrate
     * @deprecated
     */
    @Deprecated(forRemoval = true)
    @Schema(description = "机器人加签密钥", example = "key")
    String robotSecret;

    @Schema(description = "是否使用sonic hub", example = "1")
    Integer hasHub;

    @Schema(description = "通知机器人id串，为null时自动选取所有可用机器人", example = "[1,2]")
    int[] alertRobotIds;
}
