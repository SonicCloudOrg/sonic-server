package org.cloud.sonic.controller.models.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.Scripts;

import java.io.Serializable;

@ApiModel("脚本模板模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScriptsDTO implements Serializable, TypeConverter<ScriptsDTO, Scripts> {
    @ApiModelProperty(value = "id", example = "1")
    Integer id;

    @ApiModelProperty(value = "项目id", example = "1")
    Integer projectId;

    @ApiModelProperty(value = "模板名称", example = "脚本模板A")
    String name;

    @ApiModelProperty(value = "语言", example = "Java")
    String scriptLanguage;

    @ApiModelProperty(value = "脚本内容", example = "println Hello world")
    String content;
}
