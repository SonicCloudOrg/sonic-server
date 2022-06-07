package org.cloud.sonic.controller.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.Versions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.Date;

@ApiModel("版本迭代DTO 模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VersionsDTO implements Serializable, TypeConverter<VersionsDTO, Versions> {

    @ApiModelProperty(value = "id", example = "1")
    Integer id;

    @Positive
    @ApiModelProperty(value = "项目id", example = "1")
    Integer projectId;

    @NotBlank
    @ApiModelProperty(value = "迭代名称", example = "xxx迭代")
    String versionName;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "日期", example = "2021-08-15T16:00:00.000+00:00")
    Date createTime;
}
