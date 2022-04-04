package org.cloud.sonic.common.models.dto;

import org.cloud.sonic.common.models.base.TypeConverter;
import org.cloud.sonic.common.models.domain.TestSuites;
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

@ApiModel("测试套件DTO 模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestSuitesDTO implements Serializable, TypeConverter<TestSuitesDTO, TestSuites> {

    @ApiModelProperty(value = "id", example = "1")
    Integer id;

    @NotBlank
    @ApiModelProperty(value = "测试套件名称", required = true, example = "首页测试套件")
    String name;

    @Positive
    @ApiModelProperty(value = "测试套件平台类型", required = true, example = "1")
    Integer platform;

    @Positive
    @ApiModelProperty(value = "覆盖类型", required = true, example = "1")
    Integer cover;

    @Positive
    @ApiModelProperty(value = "项目id", required = true, example = "1")
    Integer projectId;

    @ApiModelProperty(value = "包含的测试用例")
    List<TestCasesDTO> testCases;

    @ApiModelProperty(value = "指定设备列表")
    List<DevicesDTO> devices;
}
