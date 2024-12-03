package org.cloud.sonic.controller.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.StepsElements;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author JayWenStar
 * @since 2021-12-17
 */
@Schema(name = "StepsElementsDTO 对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepsElementsDTO implements Serializable, TypeConverter<StepsElementsDTO, StepsElements> {

    private Integer stepsId;

    private Integer elementsId;
}
