package com.sonic.controller.models.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlCharsetConstant;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlEngineConstant;
import com.sonic.controller.models.base.TypeConverter;
import com.sonic.controller.models.dto.ModulesDTO;
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
@ApiModel(value = "Modules对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("modules")
@TableComment("模块表")
@TableCharset(MySqlCharsetConstant.DEFAULT)
@TableEngine(MySqlEngineConstant.InnoDB)
public class Modules implements Serializable, TypeConverter<Modules, ModulesDTO> {

    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    private Integer id;

    @TableField
    private String name;

    @TableField
    @Column(value = "project_id", isNull = false, comment = "所属项目名称")
    @Index(value = "IDX_PROJECT_ID", columns = {"project_id"})
    private Integer projectId;
}
