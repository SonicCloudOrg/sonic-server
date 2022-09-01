package org.cloud.sonic.controller.models.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.TestCases;
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
import java.util.Date;
import java.util.List;

@ApiModel("测试用例模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCasesDTO implements Serializable, TypeConverter<TestCasesDTO, TestCases> {

    @ApiModelProperty(value = "id", example = "1")
    Integer id;

    @NotBlank
    @ApiModelProperty(value = "用例名称", required = true, example = "测试用例")
    String name;

    @Positive
    @ApiModelProperty(value = "所属平台", required = true, example = "1")
    Integer platform;

    @Positive
    @ApiModelProperty(value = "项目id", required = true, example = "1")
    Integer projectId;

    @ApiModelProperty(value = "模块名称", required = false, example = "xxx模块")
    Integer moduleId;

    @ApiModelProperty(value = "项目迭代名称", required = true, example = "v1.0.0需求增加")
    String version;

    @ApiModelProperty(value = "用例描述", required = true, example = "xxx测试用例描述")
    String des;

    @ApiModelProperty(value = "用例设计人", required = true, example = "YiXunZhou")
    String designer;

    @ApiModelProperty(value = "最后修改日期", required = true, example = "2021-08-15 11:10:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date editTime;

    @JsonIgnore
    @JSONField(serialize = false)
    List<TestSuitesDTO> testSuites;

    @JsonIgnore
    @JSONField(serialize = false)
    ModulesDTO modulesDTO;
}
