package com.sonic.controller.models.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlCharsetConstant;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlEngineConstant;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.sonic.controller.models.base.TypeConverter;
import com.sonic.controller.models.dto.StepsDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author JayWenStar
 * @since 2021-12-17
 */
@ApiModel(value = "Steps对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("steps")
@TableComment("测试步骤表")
@TableCharset(MySqlCharsetConstant.DEFAULT)
@TableEngine(MySqlEngineConstant.InnoDB)
public class Steps implements Serializable, TypeConverter<Steps, StepsDTO> {

    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    private Integer id;

    @TableField
    @Column(value = "case_id", isNull = false, comment = "所属测试用例id")
    @Index(value = "IDX_CASE_ID", columns = {"case_id"})
    private Integer caseId;

    @TableField
    @Column(type = MySqlTypeConstant.LONGTEXT, isNull = false, comment = "输入文本")
    private String content;

    @TableField
    @Column(isNull = false, comment = "异常处理类型")
    private Integer error;

    @TableField
    @Column(isNull = false, comment = "设备系统类型")
    private Integer platform;

    @TableField
    @Column(value = "project_id", isNull = false, comment = "所属项目id")
    @Index(value = "IDX_PROJECT_ID", columns = {"project_id"})
    private Integer projectId;

    @TableField
    @Column(isNull = false, comment = "排序号")
    private Integer sort;

    @TableField
    @Column(value = "step_type", isNull = false, comment = "步骤类型")
    private String stepType;

    @TableField
    @Column(type = MySqlTypeConstant.LONGTEXT, isNull = false, comment = "其它信息")
    private String text;
}
