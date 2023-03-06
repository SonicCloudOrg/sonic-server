package org.cloud.sonic.controller.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.Resources;

import java.io.Serializable;
import java.util.List;

@Schema(name = "请求资源DTO 模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourcesDTO implements Serializable, TypeConverter<ResourcesDTO, Resources> {

    @Schema(description = "id", example = "1")
    private Integer id;

    @Schema(description = "desc", example = "描述")
    private String desc;

    @Schema(description = "parentId", example = "父级id")
    private Integer parentId;

    @Schema(description = "path", example = "资源路径")
    private String path;

    @Schema(description = "method", example = "请求方法")
    private String method;

    @Schema(description = "needAuth", example = "是否需要鉴权")
    private Integer needAuth;

    @Schema(description = "hasAuth", example = "当前角色是否有这个资源的权限")
    private Boolean hasAuth;

    @Schema(description = "child", example = "当前")
    private List<ResourcesDTO> child;


}
