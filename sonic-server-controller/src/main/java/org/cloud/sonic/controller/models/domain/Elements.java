package org.cloud.sonic.controller.models.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.dto.ElementsDTO;
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
@ApiModel(value = "Elements对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("elements")
public class Elements implements Serializable, TypeConverter<Elements, ElementsDTO> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField
    private String eleName;

    @TableField
    private String eleType;

    @TableField
    private String eleValue;

    @TableField
    private Integer projectId;
}
