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
import org.cloud.sonic.controller.models.domain.GlobalParams;

import java.io.Serializable;

@Schema(name = "全局参数DTO 模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlobalParamsDTO implements Serializable, TypeConverter<GlobalParamsDTO, GlobalParams> {

    @Schema(description = "id", example = "1")
    Integer id;

    @Positive
    @Schema(description = "项目id", required = true, example = "1")
    Integer projectId;

    @NotBlank
    @Schema(description = "参数名", required = true, example = "account")
    String paramsKey;

    @NotBlank
    @Schema(description = "参数值", required = true, example = "123456789")
    String paramsValue;
}
