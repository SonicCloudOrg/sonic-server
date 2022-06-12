package org.cloud.sonic.controller.models.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.Resources;

import java.io.Serializable;
import java.util.List;

@ApiModel("请求资源DTO 模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourcesDTO implements Serializable, TypeConverter<ResourcesDTO, Resources> {

    @ApiModelProperty(value = "id", example = "1")
    private Integer id;

    @ApiModelProperty(value = "desc", example = "描述")
    private String desc;

    @ApiModelProperty(value = "parentId", example = "父级id")
    private Integer parentId;

    @ApiModelProperty(value = "path", example = "资源路径")
    private String path;

    @ApiModelProperty(value = "method", example = "请求方法")
    private String method;

    @ApiModelProperty(value = "needAuth", example = "是否需要鉴权")
    private Integer needAuth;

    @ApiModelProperty(value = "hasAuth", example = "当前角色是否有这个资源的权限")
    private Boolean hasAuth;

    @ApiModelProperty(value = "child", example = "当前")
    private List<ResourcesDTO> child;


}
