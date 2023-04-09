package org.cloud.sonic.controller.models.http;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.io.Serializable;

@Schema(name = "占用安卓设备请求模型")
@Data
public class OccupyParams implements Serializable {
    @NotNull
    @Schema(description = "设备udId", required = true, example = "xxxx")
    private String udId;

    @Positive
    @Schema(description = "设备平台(Android 1, iOS 2)", required = true, example = "xxxx")
    private int platform;

    @Schema(description = "设备adb远程连接端口（可选，0时不暴露也不启动对应服务，platform为1时有效）", example = "1234")
    private int sasRemotePort;

    @Schema(description = "设备uia2 server远程连接端口（可选，0时不暴露也不启动对应服务，platform为1时有效）", example = "1234")
    private int uia2RemotePort;

    @Schema(description = "设备sib远程连接端口（可选，0时不暴露也不启动对应服务，platform为2时有效）", example = "1234")
    private int sibRemotePort;

    @Schema(description = "设备wda server远程连接端口（可选，0时不暴露也不启动对应服务，platform为2时有效）", example = "1234")
    private int wdaServerRemotePort;

    @Schema(description = "设备wda mjpeg远程连接端口（可选，0时不暴露也不启动对应服务，platform为2时有效）", example = "1234")
    private int wdaMjpegRemotePort;
}
