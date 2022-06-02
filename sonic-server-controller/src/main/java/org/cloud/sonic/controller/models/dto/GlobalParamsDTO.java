package org.cloud.sonic.controller.models.dto;

import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.GlobalParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@ApiModel("全局参数DTO 模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlobalParamsDTO implements Serializable, TypeConverter<GlobalParamsDTO, GlobalParams> {

    @ApiModelProperty(value = "id", example = "1")
    Integer id;

    @Positive
    @ApiModelProperty(value = "项目id", required = true, example = "1")
    Integer projectId;

    @NotBlank
    @ApiModelProperty(value = "参数名", required = true, example = "account")
    String paramsKey;

    @NotBlank
    @ApiModelProperty(value = "参数值", required = true, example = "123456789")
    String paramsValue;
}
