package org.cloud.sonic.controller.models.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.dto.ProjectsDTO;
import io.swagger.annotations.ApiModel;
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
@ApiModel(value = "Projects对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("projects")
public class Projects implements Serializable, TypeConverter<Projects, ProjectsDTO> {

    @TableId(value = "id", type = IdType.AUTO)
//    @IsAutoIncrement
    private Integer id;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date editTime;

    @TableField
    private String projectDes;

    @TableField
    private String projectImg;

    @TableField
    private String projectName;

    @TableField
    private String robotSecret;

    @TableField
    private String robotToken;

    @TableField
    private Integer robotType;
}
