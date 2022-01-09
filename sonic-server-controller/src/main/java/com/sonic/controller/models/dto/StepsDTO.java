package com.sonic.controller.models.dto;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sonic.controller.models.base.TypeConverter;
import com.sonic.controller.models.domain.Steps;
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
import java.util.List;

@ApiModel("运行步骤DTO 模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepsDTO implements Serializable, TypeConverter<StepsDTO, Steps> {

    @ApiModelProperty(value = "id", example = "1")
    Integer id;

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

    @ApiModelProperty(value = "包含元素列表")
    List<ElementsDTO> elements;

    @JsonIgnore
    @JSONField(serialize = false)
    List<PublicStepsDTO> publicSteps;

    @ApiModelProperty(value = "所属测试用例")
    TestCasesDTO testCasesDTO;
}
