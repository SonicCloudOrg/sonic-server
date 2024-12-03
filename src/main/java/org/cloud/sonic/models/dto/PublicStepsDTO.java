package org.cloud.sonic.controller.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.PublicSteps;

import java.io.Serializable;
import java.util.List;

@Schema(name = "公共步骤模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicStepsDTO implements Serializable, TypeConverter<PublicStepsDTO, PublicSteps> {

    @Schema(description = "id", example = "1")
    Integer id;

    @Positive
    @Schema(description = "项目id", required = true, example = "1")
    Integer projectId;

    @Positive
    @Schema(description = "平台", required = true, example = "1")
    Integer platform;

    @NotBlank
    @Schema(description = "公共步骤名称", required = true, example = "登陆步骤")
    String name;

    @Schema(description = "包含操作步骤列表")
    List<StepsDTO> steps;
}
