package org.cloud.sonic.common.models.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.common.models.base.TypeConverter;
import org.cloud.sonic.common.models.domain.Cabinet;

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
}
