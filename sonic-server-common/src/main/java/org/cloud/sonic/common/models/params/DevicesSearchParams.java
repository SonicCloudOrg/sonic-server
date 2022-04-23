package org.cloud.sonic.common.models.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 设备搜索参数
 *
 * @author JayWenStar
 * @date 2021/12/22 9:56 下午
 */
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DevicesSearchParams implements Serializable {

    @ApiModelProperty("ios版本")
    private List<String> iOSVersion;

    @ApiModelProperty("android版本")
    private List<String> androidVersion;

    @ApiModelProperty("制造商")
    private List<String> manufacturer;

    @ApiModelProperty("cpu类型")
    private List<String> cpu;

    @ApiModelProperty("屏幕尺寸")
    private List<String> size;

    @ApiModelProperty("所在Agent")
    private List<Integer> agentId;

    @ApiModelProperty("当前状态")
    private List<String> status;

    @ApiModelProperty("设备udId等信息")
    private String deviceInfo;
}
