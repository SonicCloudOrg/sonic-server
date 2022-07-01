package org.cloud.sonic.controller.models.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.dto.ResourcesDTO;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("resources")
public class Resources implements Serializable, TypeConverter<Resources, ResourcesDTO> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("`desc`")
    private String desc;

    @TableField
    private Integer parentId;

    @TableField
    //@Column(value = "method", isNull = false, comment = "请求方法")
//    @Index
    private String method;

    @TableField
    private String path;

    @TableField
    private Integer white;

    @TableField
    private Integer needAuth;

    @ApiModelProperty(value = "创建时间", example = "2021-08-15 11:36:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    Date createTime;

    @ApiModelProperty(value = "更新时间", example = "2021-08-15 11:36:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    Date updateTime;


}
