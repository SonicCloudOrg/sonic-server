package org.cloud.sonic.controller.models.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.ProjectsMembers;

import java.io.Serializable;


/**
 * @author liulijun
 * @since 2023-4-28
 */
@Schema(name = "项目成员DTO 模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectsMembersDTO implements Serializable, TypeConverter<ProjectsMembersDTO, ProjectsMembers> {

    @Schema(description = "id", example = "1")
    Integer id;

    @Schema(description = "项目ID", required = true, example = "1")
    Integer projectId;

    @Schema(description = "用户ID", required = true, example = "1")
    Integer userId;

    @Schema(description = "用户名称", required = true, example = "llj")
    String userName;

    @Schema(description = "成员角色", required = true, example = "1")
    Integer memberRole;

}
