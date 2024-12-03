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
import org.cloud.sonic.controller.models.domain.Modules;

import java.io.Serializable;

@Schema(name = "模块模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModulesDTO implements Serializable, TypeConverter<ModulesDTO, Modules> {

    @Schema(description = "id", example = "1")
    Integer id;

    @Positive
    @Schema(description = "项目id", required = true, example = "1")
    Integer projectId;

    @NotBlank
    @Schema(description = "模块名称", required = true, example = "首页")
    String name;
}
