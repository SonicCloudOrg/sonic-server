package org.cloud.sonic.controller.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.Versions;

import java.io.Serializable;
import java.util.Date;

@Schema(name = "版本迭代DTO 模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VersionsDTO implements Serializable, TypeConverter<VersionsDTO, Versions> {

    @Schema(description = "id", example = "1")
    Integer id;

    @Positive
    @Schema(description = "项目id", example = "1")
    Integer projectId;

    @NotBlank
    @Schema(description = "迭代名称", example = "xxx迭代")
    String versionName;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "日期", example = "2021-08-15T16:00:00.000+00:00")
    Date createTime;
}
