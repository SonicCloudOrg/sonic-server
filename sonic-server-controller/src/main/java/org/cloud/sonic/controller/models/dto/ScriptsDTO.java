package org.cloud.sonic.controller.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.Scripts;

import java.io.Serializable;

@Schema(name = "脚本模板模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScriptsDTO implements Serializable, TypeConverter<ScriptsDTO, Scripts> {
    @Schema(description = "id", example = "1")
    Integer id;

    @Schema(description = "项目id", example = "1")
    Integer projectId;

    @Schema(description = "模板名称", example = "脚本模板A")
    String name;

    @Schema(description = "语言", example = "Java")
    String scriptLanguage;

    @Schema(description = "脚本内容", example = "println Hello world")
    String content;
}
