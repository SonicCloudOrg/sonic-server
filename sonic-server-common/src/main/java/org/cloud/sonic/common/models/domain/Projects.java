package org.cloud.sonic.common.models.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.sunchenbin.mybatis.actable.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlCharsetConstant;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlEngineConstant;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import org.cloud.sonic.common.models.base.TypeConverter;
import org.cloud.sonic.common.models.dto.ProjectsDTO;
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
@TableComment("项目表")
@TableCharset(MySqlCharsetConstant.DEFAULT)
@TableEngine(MySqlEngineConstant.InnoDB)
public class Projects implements Serializable, TypeConverter<Projects, ProjectsDTO> {

    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    private Integer id;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(value = "edit_time", type = MySqlTypeConstant.DATETIME, comment = "更改时间")
    private Date editTime;

    @TableField
    @Column(value = "project_des", isNull = false, comment = "项目描述")
    private String projectDes;

    @TableField
    @Column(value = "project_img", isNull = false, comment = "项目封面")
    private String projectImg;

    @TableField
    @Column(value = "project_name", isNull = false, comment = "项目名")
    private String projectName;

    @TableField
    @Column(value = "robot_secret", isNull = false, comment = "机器人秘钥")
    private String robotSecret;

    @TableField
    @Column(value = "robot_token", isNull = false, comment = "机器人token")
    private String robotToken;

    @TableField
    @Column(value = "robot_type", isNull = false, comment = "机器人类型")
    private Integer robotType;
}
