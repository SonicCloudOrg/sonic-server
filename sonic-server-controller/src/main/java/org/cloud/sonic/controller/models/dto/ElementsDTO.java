package org.cloud.sonic.controller.models.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.Elements;

import java.io.Serializable;
import java.util.List;

@Schema(name = "页面控件DTO 模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElementsDTO implements Serializable, TypeConverter<ElementsDTO, Elements> {

    @Schema(description = "id", example = "1")
    Integer id;

    @Schema(description = "所属steps的id", example = "1")
    Integer stepsId;

    @NotBlank
    @Schema(description = "控件名称", required = true, example = "首页底部按钮")
    String eleName;

    @Schema(description = "控件类型", required = true, example = "xpath")
    String eleType;

    @Schema(description = "控件值", required = true, example = "//@[text()='home']")
    String eleValue;

    @Positive
    @Schema(description = "项目id", required = true, example = "1")
    Integer projectId;

    @Schema(description = "模块id", required = false, example = "1")
    Integer moduleId;

    @JSONField(serialize = false)
    ModulesDTO modulesDTO;

    //因为一个控件可以存在于多个步骤，也可以一个步骤有多个同样的控件，所以是多对多关系
    @JsonIgnore
    @JSONField(serialize = false)
    List<StepsDTO> steps;
}
