package org.cloud.sonic.controller.models.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.dto.StepsDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.enums.ConditionEnum;

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
public class Steps implements Serializable, TypeConverter<Steps, StepsDTO> {

    @TableId(value = "id", type = IdType.AUTO)
//    @IsAutoIncrement
    private Integer id;

    @TableField
    private Integer parentId;

    @TableField
    private Integer caseId;

    @TableField
    private String content;

    @TableField
    private Integer error;

    @TableField
    private Integer platform;

    @TableField
    private Integer projectId;

    @TableField
    private Integer sort;

    @TableField
    private String stepType;

    @TableField
    private String text;

    /**
     * @see ConditionEnum
     */
    @TableField
    private Integer conditionType;
}
