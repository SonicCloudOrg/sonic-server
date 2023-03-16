package org.cloud.sonic.controller.models.dto;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.Steps;
import org.cloud.sonic.controller.models.enums.ConditionEnum;

import java.io.Serializable;
import java.util.List;

@Schema(name = "运行步骤DTO 模型")
@Getter
@Setter
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepsDTO implements Serializable, TypeConverter<StepsDTO, Steps> {

    @Schema(description = "id", example = "1")
    Integer id;

    @Schema(description = "父级id，一般父级都是条件步骤", example = "0")
    Integer parentId;

    @Positive
    @Schema(description = "项目id", required = true, example = "1")
    Integer projectId;

    @Schema(description = "所属公共步骤id", required = true, example = "1")
    Integer publicStepsId;

    @Schema(description = "测试用例id", example = "1")
    Integer caseId;

    @Positive
    @Schema(description = "类型", required = true, example = "1")
    Integer platform;

    @NotNull
    @Schema(description = "步骤类型", required = true, example = "click")
    String stepType;

    @Schema(description = "输入文本", required = true, example = "123")
    String content;

    @Schema(description = "其他信息", required = true, example = "456")
    String text;

    @Schema(description = "排序号", example = "123")
    int sort;

    @Positive
    @Schema(description = "异常处理类型", required = true, example = "1")
    int error;

    /**
     * @see ConditionEnum
     */
    @Schema(description = "步骤条件类型，0：非条件  1：if  2：else if  3：else  4：while", example = "0")
    private Integer conditionType;

    @Schema(description = "是否禁用", example = "0")
    private Integer disabled;

    @Schema(description = "包含元素列表")
    List<ElementsDTO> elements;

    @JsonIgnore
    @JSONField(serialize = false)
    List<PublicStepsDTO> publicSteps;

    @Schema(description = "所属测试用例")
    TestCasesDTO testCasesDTO;

    @Schema(description = "子步骤")
    List<StepsDTO> childSteps;
}
