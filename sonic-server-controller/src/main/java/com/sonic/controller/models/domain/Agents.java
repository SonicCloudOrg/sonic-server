package com.sonic.controller.models.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlCharsetConstant;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlEngineConstant;
import com.sonic.controller.models.base.TypeConverter;
import com.sonic.controller.models.dto.AgentsDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author JayWenStar,Eason
 * @since 2021-12-17
 */
@ApiModel(value = "Agents对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("agents")
@TableComment("agents表")
@TableCharset(MySqlCharsetConstant.DEFAULT)
@TableEngine(MySqlEngineConstant.InnoDB)
public class Agents implements Serializable, TypeConverter<Agents, AgentsDTO> {

    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    private Integer id;

    @TableField
    @Column(isNull = false, comment = "agent的ip")
    private String host;

    @TableField
    @Column(isNull = false, comment = "agent name")
    private String name;

    @TableField
    @Column(isNull = false, comment = "agent的端口")
    private Integer port;

    @TableField
    @Column(value = "secret_key", comment = "agent的密钥", defaultValue = "")
    private String secretKey;

    @TableField
    @Column(isNull = false, comment = "agent的状态")
    private Integer status;

    @TableField
    @Column(value = "system_type", isNull = false, comment = "agent的系统类型")
    private String systemType;

    @TableField
    @Column(isNull = false, comment = "agent端代码版本", defaultValue = "")
    private String version;
}
