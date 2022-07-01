package org.cloud.sonic.controller.models.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.dto.VersionsDTO;
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
@ApiModel(value = "Versions对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("versions")
public class Versions implements Serializable, TypeConverter<Versions, VersionsDTO> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "日期", example = "2021-08-15T16:00:00.000+00:00")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField
    private Integer projectId;

    @TableField
    private String versionName;
}
