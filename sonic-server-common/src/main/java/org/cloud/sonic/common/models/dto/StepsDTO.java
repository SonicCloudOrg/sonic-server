package org.cloud.sonic.common.models.dto;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.cloud.sonic.common.models.base.TypeConverter;
import org.cloud.sonic.common.models.domain.Steps;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;
import org.cloud.sonic.common.models.enums.ConditionEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.List;

@ApiModel("运行步骤DTO 模型")
@Getter
@Setter
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepsDTO implements Serializable, TypeConverter<StepsDTO, Steps> {

    @ApiModelProperty(value = "id", example = "1")
    Integer id;

    @ApiModelProperty(value = "父级id，一般父级都是条件步骤", example = "0")
    Integer parentId;

    @Positive
    @ApiModelProperty(value = "项目id", required = true, example = "1")
    Integer projectId;

    @ApiModelProperty(value = "所属公共步骤id", required = true, example = "1")
    Integer publicStepsId;

    @ApiModelProperty(value = "测试用例id", example = "1")
    Integer caseId;

    @Positive
    @ApiModelProperty(value = "类型", required = true, example = "1")
    Integer platform;

    @NotBlank
    @ApiModelProperty(value = "步骤类型", required = true, example = "click")
    String stepType;

    @ApiModelProperty(value = "输入文本", required = true, example = "123")
    String content;

    @ApiModelProperty(value = "其他信息", required = true, example = "456")
    String text;

    @ApiModelProperty(value = "排序号", example = "123")
    int sort;

    @Positive
    @ApiModelProperty(value = "异常处理类型", required = true, example = "1")
    int error;

    /**
     * @see ConditionEnum
     */
    @ApiModelProperty(value = "步骤条件类型，0：非条件  1：if  2：else if  3：else  4：while", example = "0")
    private Integer conditionType;

    @ApiModelProperty(value = "包含元素列表")
    List<ElementsDTO> elements;

    @JsonIgnore
    @JSONField(serialize = false)
    List<PublicStepsDTO> publicSteps;

    @ApiModelProperty(value = "所属测试用例")
    TestCasesDTO testCasesDTO;

    @ApiModelProperty(value = "子步骤")
    List<StepsDTO> childSteps;
}
