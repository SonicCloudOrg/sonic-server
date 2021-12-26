package com.sonic.controller.models.dto;

import com.sonic.controller.models.base.TypeConverter;
import com.sonic.controller.models.domain.Modules;
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

@ApiModel("模块模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModulesDTO implements Serializable, TypeConverter<ModulesDTO, Modules> {

    @ApiModelProperty(value = "id", example = "1")
    Integer id;

    @Positive
    @ApiModelProperty(value = "项目id", required = true, example = "1")
    Integer projectId;

    @NotBlank
    @ApiModelProperty(value = "模块名称", required = true, example = "首页")
    String name;
}
