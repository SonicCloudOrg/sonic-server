package org.cloud.sonic.controller.models.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.dto.AgentsDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author JayWenStar, Eason
 * @since 2021-12-17
 */
@ApiModel(value = "Agents对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("agents")
public class Agents implements Serializable, TypeConverter<Agents, AgentsDTO> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField
    private String host;

    @TableField
    private String name;

    @TableField
    private Integer port;

    @TableField
    private Integer rpcPort;

    @TableField
    private String secretKey;

    @TableField
    private Integer status;

    @TableField
    private String systemType;

    @TableField
    private String version;

    @TableField
    private Long lockVersion;

    @TableField
    private Integer cabinetId;

    @TableField
    private Integer storey;
}
