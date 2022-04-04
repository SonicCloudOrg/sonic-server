package org.cloud.sonic.common.models.dto;

import org.cloud.sonic.common.models.base.TypeConverter;
import org.cloud.sonic.common.models.domain.Jobs;
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

@ApiModel("定时任务DTO 实体")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobsDTO implements Serializable, TypeConverter<JobsDTO, Jobs> {

    @ApiModelProperty(value = "id", example = "1")
    Integer id;

    @NotBlank
    @ApiModelProperty(value = "定时任务名称", required = true, example = "每周三跑一次")
    String name;

    @Positive
    @ApiModelProperty(value = "套件id", required = true, example = "123")
    Integer suiteId;

    @Positive
    @ApiModelProperty(value = "项目id", required = true, example = "1")
    Integer projectId;

    @ApiModelProperty(value = "状态（1为开启，2为关闭）", example = "1")
    Integer status;

    @NotBlank
    @ApiModelProperty(value = "cron表达式", required = true, example = "* 30 * * * ?")
    String cronExpression;
}