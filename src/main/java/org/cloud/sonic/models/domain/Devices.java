/*
 *   sonic-server  Sonic Cloud Real Machine Platform.
 *   Copyright (C) 2022 SonicCloudOrg
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.cloud.sonic.controller.models.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlCharsetConstant;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlEngineConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.dto.DevicesDTO;

import java.io.Serializable;

/**
 * @author JayWenStar
 * @since 2021-12-17
 */
@Schema(name ="Devices对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("devices")
@TableComment("设备表")
@TableCharset(MySqlCharsetConstant.DEFAULT)
@TableEngine(MySqlEngineConstant.InnoDB)
public class Devices implements Serializable, TypeConverter<Devices, DevicesDTO> {

    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    private Integer id;

    @TableField
    @Column(value = "agent_id", isNull = false, comment = "所属agent的id")
    private Integer agentId;

    @TableField
    @Column(comment = "cpu架构", defaultValue = "")
    private String cpu;

    @TableField
    @Column(value = "img_url", comment = "手机封面", defaultValue = "")
    private String imgUrl;

    @TableField
    @Column(comment = "制造商", defaultValue = "")
    private String manufacturer;

    @TableField
    @Column(comment = "手机型号", defaultValue = "")
    private String model;

    @TableField
    @Column(comment = "设备名称", defaultValue = "")
    private String name;

    @TableField
    @Column(comment = "设备安装app的密码", defaultValue = "")
    private String password;

    @TableField
    @Column(isNull = false, comment = "系统类型 1：android 2：ios")
    private Integer platform;

    @TableField
    @Column(value = "is_hm", isNull = false, comment = "是否为鸿蒙类型 1：鸿蒙 0：非鸿蒙", defaultValue = "0")
    private Integer isHm;

    @TableField
    @Column(comment = "设备分辨率", defaultValue = "")
    private String size;

    @TableField
    @Column(comment = "设备状态", defaultValue = "")
    private String status;

    @TableField
    @Column(value = "ud_id", comment = "设备序列号", defaultValue = "")
    @Index(value = "IDX_UD_ID", columns = {"ud_id"})
    private String udId;

    @TableField
    @Column(comment = "设备系统版本", defaultValue = "")
    private String version;

    @TableField
    @Column(value = "nick_name", comment = "设备备注", defaultValue = "")
    private String nickName;

    @TableField
    @Column(comment = "设备当前占用者", defaultValue = "")
    private String user;

    @TableField
    @Column(value = "chi_name", comment = "中文设备", defaultValue = "")
    String chiName;

    @TableField
    @Column(defaultValue = "0", comment = "设备温度")
    Integer temperature;

    @TableField
    @Column(defaultValue = "0", comment = "设备电池电压")
    Integer voltage;

    @TableField
    @Column(defaultValue = "0", comment = "设备电量")
    Integer level;

    @TableField
    @Column(defaultValue = "0", comment = "HUB位置")
    Integer position;

    public static Devices newDeletedDevice(int id) {
        String tips = "Device does not exist.";
        return new Devices()
                .setAgentId(0)
                .setStatus("DISCONNECTED")
                .setPlatform(0)
                .setIsHm(0)
                .setId(id)
                .setVersion("unknown")
                .setSize("unknown")
                .setCpu("unknown")
                .setManufacturer("unknown")
                .setName(tips)
                .setModel(tips)
                .setChiName(tips)
                .setNickName(tips)
                .setName(tips)
                .setUser(tips)
                .setUdId(tips)
                .setPosition(0)
                .setTemperature(0)
                .setVoltage(0)
                .setLevel(0);
    }
}
