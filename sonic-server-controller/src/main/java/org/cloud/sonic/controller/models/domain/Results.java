package org.cloud.sonic.controller.models.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.dto.ResultsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author JayWenStar
 * @since 2021-12-17
 */
@ApiModel(value = "Results对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("results")
public class Results implements Serializable, TypeConverter<Results, ResultsDTO> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "创建时间", example = "2021-08-15 11:36:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    Date createTime;

    @ApiModelProperty(value = "结束时间", example = "2021-08-15 11:36:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    Date endTime;

    @TableField
    private Integer projectId;

    @TableField
    private Integer receiveMsgCount;

    @TableField
    private Integer sendMsgCount;

    @TableField
    private Integer status;

    @TableField
    private String strike;

    @TableField
    private Integer suiteId;

    @TableField
    private String suiteName;
}
