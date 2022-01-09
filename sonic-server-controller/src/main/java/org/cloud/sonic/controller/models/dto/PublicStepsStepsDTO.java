package org.cloud.sonic.controller.models.dto;

import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.PublicStepsSteps;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author JayWenStar
 * @since 2021-12-17
 */
@ApiModel(value = "PublicStepsStepsDTO 对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicStepsStepsDTO implements Serializable, TypeConverter<PublicStepsStepsDTO, PublicStepsSteps> {

    private Integer publicStepsId;

    private Integer stepsId;
}
