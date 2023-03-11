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
import org.cloud.sonic.controller.models.domain.Jobs;

import java.io.Serializable;

@Schema(name = "定时任务DTO 实体")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobsDTO implements Serializable, TypeConverter<JobsDTO, Jobs> {

    @Schema(description = "id", example = "1")
    Integer id;

    @NotBlank
    @Schema(description = "定时任务名称", required = true, example = "每周三跑一次")
    String name;

    @Positive
    @Schema(description = "套件id", required = true, example = "123")
    Integer suiteId;

    @Positive
    @Schema(description = "项目id", required = true, example = "1")
    Integer projectId;

    @Schema(description = "状态（1为开启，2为关闭）", example = "1")
    Integer status;

    @NotBlank
    @Schema(description = "cron表达式", required = true, example = "* 30 * * * ?")
    String cronExpression;
}