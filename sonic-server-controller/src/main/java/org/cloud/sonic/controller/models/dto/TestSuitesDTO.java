package org.cloud.sonic.controller.models.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.TestSuites;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @NotNull
    @ApiModelProperty(value = "是否采集系统性能数据", required = true, example = "1")
    Integer isOpenPerfmon;

    @NotNull
    @ApiModelProperty(value = "采集性能数据间隔", required = true, example = "1")
    Integer perfmonInterval;

    @ApiModelProperty(value = "包含的测试用例")
    List<TestCasesDTO> testCases;

    @ApiModelProperty(value = "指定设备列表")
    List<DevicesDTO> devices;
}
