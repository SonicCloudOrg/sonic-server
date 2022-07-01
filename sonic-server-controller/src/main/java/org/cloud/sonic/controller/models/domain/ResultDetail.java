package org.cloud.sonic.controller.models.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.dto.ResultDetailDTO;
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
@ApiModel(value = "ResultDetail对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("result_detail")
public class ResultDetail implements Serializable, TypeConverter<ResultDetail, ResultDetailDTO> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField
    private Integer caseId;

    @TableField
    private String des;

    @TableField
    private Integer deviceId;

    @TableField
    private String log;

    @TableField
    private Integer resultId;

    @TableField
    private Integer status;

    @ApiModelProperty(value = "时间", example = "16:00:00")
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    @TableField
    private Date time;

    @TableField
    private String type;
}
