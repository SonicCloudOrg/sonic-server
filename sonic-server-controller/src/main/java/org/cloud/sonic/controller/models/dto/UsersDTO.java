package org.cloud.sonic.controller.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.Users;

import java.io.Serializable;

@Schema(name = "用户DTO 模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersDTO implements Serializable, TypeConverter<UsersDTO, Users> {

    @Schema(description = "id", example = "1")
    Integer id;

    @NotBlank
    @Schema(description = "用户名称", required = true, example = "ZhouYiXun")
    String userName;

    @NotBlank
    @Schema(description = "用户密码", required = true, example = "123456")
    String password;

    @Positive
    @Schema(description = "角色", required = false, example = "1")
    Integer role;

    @Schema(description = "角色名称", required = false, example = "tester")
    String roleName;
}
