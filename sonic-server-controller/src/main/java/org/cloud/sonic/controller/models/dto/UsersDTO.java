package org.cloud.sonic.controller.models.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.Users;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@ApiModel("用户DTO 模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersDTO implements Serializable, TypeConverter<UsersDTO, Users> {

    @ApiModelProperty(value = "id", example = "1")
    Integer id;

    @NotBlank
    @ApiModelProperty(value = "用户名称", required = true, example = "ZhouYiXun")
    String userName;

    @NotBlank
    @ApiModelProperty(value = "用户密码", required = true, example = "123456")
    String password;

    @Positive
    @ApiModelProperty(value = "角色", required = false, example = "1")
    Integer role;

    @ApiModelProperty(value = "角色名称", required = false, example = "tester")
    String roleName;
}
