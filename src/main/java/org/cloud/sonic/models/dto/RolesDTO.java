package org.cloud.sonic.controller.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.Roles;

import java.io.Serializable;

@Schema(name = "角色模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolesDTO implements Serializable, TypeConverter<RolesDTO, Roles> {

    @Schema(description = "id", example = "1")
    private Integer id;

    @Schema(description = "角色名称", example = "角色名称")
    private String roleName;

    @Schema(description = "描述", example = "描述")
    private String comment;


}
