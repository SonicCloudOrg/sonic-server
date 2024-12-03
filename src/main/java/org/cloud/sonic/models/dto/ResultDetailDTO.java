package org.cloud.sonic.controller.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.ResultDetail;

import java.io.Serializable;
import java.util.Date;

@Schema(name = "测试结果详情DTO 模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultDetailDTO implements Serializable, TypeConverter<ResultDetailDTO, ResultDetail> {

    @Schema(description = "id", example = "1")
    Integer id;

    @Schema(description = "测试用例id", example = "1")
    Integer caseId;

    @Schema(description = "测试结果id", example = "1")
    Integer resultId;

    @Schema(description = "测试结果详情类型", example = "step")
    String type;

    @Schema(description = "测试结果详情描述", example = "点击xxx")
    String des;

    @Schema(description = "测试结果详情状态", example = "1")
    Integer status;

    @Schema(description = "设备id", example = "1")
    Integer deviceId;

    @Schema(description = "测试结果详情详细日志", example = "点击xpath://*[@text()='xxx']")
    String log;

    @Schema(description = "时间", example = "16:00:00")
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    Date time;
}
