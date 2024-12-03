package org.cloud.sonic.controller.models.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.Devices;

import java.io.Serializable;
import java.util.Set;

@Schema(name = "设备DTO 模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DevicesDTO implements Serializable, TypeConverter<DevicesDTO, Devices> {

    @Schema(description = "id", example = "1")
    Integer id;

    @Schema(description = "设备名称", example = "My HUAWEI")
    String name;

    @Schema(description = "设备备注", example = "My HUAWEI")
    String nickName;

    @Schema(description = "型号", example = "HUAWEI MATE 40")
    String model;

    @Schema(description = "序列号", example = "random")
    String udId;

    @Schema(description = "设备状态", example = "ONLINE")
    String status;

    @Schema(description = "所属Agent", example = "1")
    Integer agentId;

    @Schema(description = "设备系统", example = "1")
    Integer platform;

    @Schema(description = "鸿蒙类型", example = "0")
    Integer isHm;

    @Schema(description = "分辨率", example = "1080x1920")
    String size;

    @Schema(description = "系统版本", example = "12")
    String version;

    @Schema(description = "cpu", example = "arm64")
    String cpu;

    @Schema(description = "制造商", example = "HUAWEI")
    String manufacturer;

    @Schema(description = "安装密码", example = "123456")
    String password;

    @Schema(description = "设备图片路径")
    String imgUrl;

    @JsonIgnore
    @JSONField(serialize = false)
    Set<TestSuitesDTO> testSuites;

    @Schema(description = "设备占用者")
    String user;

    @Getter(AccessLevel.NONE)
    @Schema(description = "设备温度", example = "33")
    Integer temperature;

    @Getter(AccessLevel.NONE)
    @Schema(description = "设备电池电压", example = "33")
    Integer voltage;

    @Getter(AccessLevel.NONE)
    @Schema(description = "设备电量", example = "33")
    Integer level;

    @Getter(AccessLevel.NONE)
    @Schema(description = "HUB位置", example = "1")
    Integer position;

    @Schema(description = "中文设备", example = "荣耀全网通")
    String chiName;

    public int getPosition() {
        if (position == null) {
            return 0;
        }
        return position;
    }

    public int getVoltage() {
        if (voltage == null) {
            return 0;
        }
        return voltage;
    }

    public int getTemperature() {
        if (temperature == null) {
            return 0;
        }
        return temperature;
    }

    public int getLevel() {
        if (level == null) {
            return 0;
        }
        return level;
    }
}
