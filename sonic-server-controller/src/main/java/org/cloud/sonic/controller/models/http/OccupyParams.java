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

    @Schema(description = "（仅Android设备生效）设备adb远程连接端口。非必填，为0或不填时不暴露也不启动对应服务，建议取值范围30000-65535。", example = "30000")
    private int sasRemotePort;

    @Schema(description = "（仅Android设备生效）设备uia2 server远程连接端口。非必填，为0或不填时不暴露也不启动对应服务，建议取值范围30000-65535。", example = "30001")
    private int uia2RemotePort;

    @Schema(description = "（仅iOS设备生效）设备sib远程连接端口。非必填，为0或不填时不暴露也不启动对应服务，建议取值范围30000-65535。", example = "30002")
    private int sibRemotePort;

    @Schema(description = "（仅iOS设备生效）设备wda server远程连接端口。非必填，为0或不填时不暴露也不启动对应服务，建议取值范围30000-65535。", example = "30003")
    private int wdaServerRemotePort;

    @Schema(description = "（仅iOS设备生效）设备wda mjpeg远程连接端口。非必填，为0或不填时不暴露也不启动对应服务，建议取值范围30000-65535。", example = "30004")
    private int wdaMjpegRemotePort;
}
