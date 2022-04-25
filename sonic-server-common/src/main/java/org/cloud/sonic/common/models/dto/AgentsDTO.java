package org.cloud.sonic.common.models.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.common.models.base.TypeConverter;
import org.cloud.sonic.common.models.domain.Agents;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @ApiModelProperty(value = "机柜id", example = "1")
    private Integer cabinetId;

    @ApiModelProperty(value = "机柜层数", example = "1-8")
    private Integer storey;
}
