package org.cloud.sonic.controller.models.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.PublicSteps;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.List;

@ApiModel("公共步骤模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicStepsDTO implements Serializable, TypeConverter<PublicStepsDTO, PublicSteps> {

    @ApiModelProperty(value = "id", example = "1")
    Integer id;

    @Positive
    @ApiModelProperty(value = "项目id", required = true, example = "1")
    Integer projectId;

    @Positive
    @ApiModelProperty(value = "平台", required = true, example = "1")
    Integer platform;

    @NotBlank
    @ApiModelProperty(value = "公共步骤名称", required = true, example = "登陆步骤")
    String name;

    @ApiModelProperty(value = "包含操作步骤列表")
    List<StepsDTO> steps;
}
