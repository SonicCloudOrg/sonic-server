package org.cloud.sonic.controller.models.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceParam implements Serializable {

    @ApiModelProperty("资源 id")
    @NotNull
    private Integer id;

    @ApiModelProperty("是否需要鉴权")
    @NotNull
    private Boolean needAuth;
}
