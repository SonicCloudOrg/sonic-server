package org.cloud.sonic.controller.models.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.Cabinet;

import java.io.Serializable;

@ApiModel("CabinetDTO 端模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CabinetDTO implements Serializable, TypeConverter<CabinetDTO, Cabinet> {
    @ApiModelProperty(value = "机柜id", example = "1")
    private Integer id;

    @ApiModelProperty(value = "机柜规格", example = "1")
    private Integer size;

    @ApiModelProperty(value = "机柜名称", example = "qwe")
    private String name;

    @ApiModelProperty(value = "机柜密钥", example = "qwe")
    private String secretKey;

    @ApiModelProperty(value = "lowLevel", example = "1")
    private Integer lowLevel;

    @ApiModelProperty(value = "lowGear", example = "1")
    private Integer lowGear;

    @ApiModelProperty(value = "highLevel", example = "1")
    private Integer highLevel;

    @ApiModelProperty(value = "highGear", example = "1")
    private Integer highGear;

    @ApiModelProperty(value = "highTemp", example = "1")
    private Integer highTemp;

    @ApiModelProperty(value = "highTempTime", example = "1")
    private Integer highTempTime;

    @ApiModelProperty(value = "机器人类型", required = true, example = "1")
    Integer robotType;

    @ApiModelProperty(value = "机器人token", required = true, example = "http://dingTalk.com?token=*****")
    String robotToken;

    @ApiModelProperty(value = "机器人加签密钥", required = true, example = "qwe***")
    String robotSecret;
}
