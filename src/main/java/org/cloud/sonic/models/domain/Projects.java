package org.cloud.sonic.controller.models.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.sunchenbin.mybatis.actable.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlCharsetConstant;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlEngineConstant;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.dto.ProjectsDTO;
import org.cloud.sonic.controller.tools.NullableIntArrayTypeHandler;

import java.io.Serializable;
import java.util.Date;

/**
 * @author JayWenStar
 * @since 2021-12-17
 */
@Schema(name ="Projects对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "projects", autoResultMap = true)
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

    /**
     * @see org.cloud.sonic.controller.config.mybatis.RobotConfigMigrate
     * @deprecated
     */
    @Deprecated(forRemoval = true)
    @TableField
    @Column(value = "robot_secret", isNull = true, comment = "机器人秘钥")
    private String robotSecret;

    /**
     * @see org.cloud.sonic.controller.config.mybatis.RobotConfigMigrate
     * @deprecated
     */
    @Deprecated(forRemoval = true)
    @TableField
    @Column(value = "robot_token", isNull = false, comment = "机器人token")
    private String robotToken;

    /**
     * @see org.cloud.sonic.controller.config.mybatis.RobotConfigMigrate
     * @deprecated
     */
    @Deprecated(forRemoval = true)
    @TableField
    @Column(value = "robot_type", isNull = false, comment = "机器人类型")
    private Integer robotType;

    @TableField(typeHandler = NullableIntArrayTypeHandler.class, updateStrategy = FieldStrategy.IGNORED)
    @Column(value = "testsuite_alert_robot_ids", type = MySqlTypeConstant.VARCHAR, length = 1024, comment = "项目内测试套件默认通知机器人，逗号分隔id串，为null时自动选取所有可用机器人")
    private int[] testsuiteAlertRobotIds;

    @TableField
    @Column(value = "global_robot", isNull = false, defaultValue = "1", comment = "启用全局机器人")
    private Boolean globalRobot;
}
