package org.cloud.sonic.controller.models.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

@Schema(name = "publicStep和Steps步骤关联模型")
@Getter
@Setter
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicStepsAndStepsIdDTO {

    StepsDTO stepsDTO;

    Integer Index;

}
