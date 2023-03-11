package org.cloud.sonic.controller.models.params;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceParam implements Serializable {

    @Schema(name = "资源 id")
    @NotNull
    private Integer id;

    @Schema(name = "是否需要鉴权")
    @NotNull
    private Boolean needAuth;
}
