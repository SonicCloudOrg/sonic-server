package org.cloud.sonic.controller.models.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.dto.PublicStepsStepsDTO;
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
@ApiModel(value = "PublicStepsSteps对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("public_steps_steps")
public class PublicStepsSteps implements Serializable, TypeConverter<PublicStepsSteps, PublicStepsStepsDTO> {

    @TableField
    private Integer publicStepsId;

    @TableField
    private Integer stepsId;
}
