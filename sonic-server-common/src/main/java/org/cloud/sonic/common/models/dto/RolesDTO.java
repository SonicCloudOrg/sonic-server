package org.cloud.sonic.common.models.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.common.models.base.TypeConverter;
import org.cloud.sonic.common.models.domain.Roles;

import java.io.Serializable;

@ApiModel("角色模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolesDTO implements Serializable, TypeConverter<RolesDTO, Roles> {

    @ApiModelProperty(value = "id", example = "1")
    private Integer id;

    @ApiModelProperty(value = "角色名称", example = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "描述", example = "描述")
    private String comment;


}
