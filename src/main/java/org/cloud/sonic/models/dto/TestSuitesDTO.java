package org.cloud.sonic.controller.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.TestSuites;

import java.io.Serializable;
import java.util.List;

@Schema(name = "测试套件DTO 模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestSuitesDTO implements Serializable, TypeConverter<TestSuitesDTO, TestSuites> {

    @Schema(description = "id", example = "1")
    Integer id;

    @NotBlank
    @Schema(description = "测试套件名称", required = true, example = "首页测试套件")
    String name;

    @Positive
    @Schema(description = "测试套件平台类型", required = true, example = "1")
    Integer platform;

    @Positive
    @Schema(description = "覆盖类型", required = true, example = "1")
    Integer cover;

    @Positive
    @Schema(description = "项目id", required = true, example = "1")
    Integer projectId;

    @NotNull
    @Schema(description = "是否采集系统性能数据", required = true, example = "1")
    Integer isOpenPerfmon;

    @NotNull
    @Schema(description = "采集性能数据间隔", required = true, example = "1")
    Integer perfmonInterval;

    @Schema(description = "包含的测试用例")
    List<TestCasesDTO> testCases;

    @Schema(description = "指定设备列表")
    List<DevicesDTO> devices;

    @Schema(description = "测试套件默认通知机器人id串，为null时取项目配置的默认值", example = "[1,2]")
    int[] alertRobotIds;
}
