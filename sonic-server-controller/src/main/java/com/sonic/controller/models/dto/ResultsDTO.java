package com.sonic.controller.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sonic.controller.models.base.TypeConverter;
import com.sonic.controller.models.domain.Results;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@ApiModel("测试结果模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultsDTO implements Serializable, TypeConverter<ResultsDTO, Results> {

    @ApiModelProperty(value = "id", example = "1")
    Integer id;

    @ApiModelProperty(value = "测试套件id", example = "1")
    Integer suiteId;

    @ApiModelProperty(value = "测试套件名称", example = "测试套件A")
    String suiteName;

    @ApiModelProperty(value = "项目id", example = "1")
    Integer projectId;

    @ApiModelProperty(value = "触发者", example = "ZhouYiXun")
    String strike;

    @ApiModelProperty(value = "发送的消息数量", example = "1")
    Integer sendMsgCount;

    @ApiModelProperty(value = "接收的消息数量", example = "2")
    Integer receiveMsgCount;

    @ApiModelProperty(value = "状态", example = "WARN")
    Integer status;

    @ApiModelProperty(value = "创建时间", example = "2021-08-15 11:36:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date createTime;

    @ApiModelProperty(value = "结束时间", example = "2021-08-15 11:36:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date endTime;
}
