package org.cloud.sonic.controller.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.Results;

import java.io.Serializable;
import java.util.Date;

@Schema(name = "测试结果模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultsDTO implements Serializable, TypeConverter<ResultsDTO, Results> {

    @Schema(description = "id", example = "1")
    Integer id;

    @Schema(description = "测试套件id", example = "1")
    Integer suiteId;

    @Schema(description = "测试套件名称", example = "测试套件A")
    String suiteName;

    @Schema(description = "项目id", example = "1")
    Integer projectId;

    @Schema(description = "触发者", example = "ZhouYiXun")
    String strike;

    @Schema(description = "发送的消息数量", example = "1")
    Integer sendMsgCount;

    @Schema(description = "接收的消息数量", example = "2")
    Integer receiveMsgCount;

    @Schema(description = "状态", example = "WARN")
    Integer status;

    @Schema(description = "创建时间", example = "2021-08-15 11:36:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date createTime;

    @Schema(description = "结束时间", example = "2021-08-15 11:36:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date endTime;
}
