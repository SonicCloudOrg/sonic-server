/*
 *  Copyright (C) [SonicCloudOrg] Sonic Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.cloud.sonic.controller.models.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.dto.DevicesDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author JayWenStar
 * @since 2021-12-17
 */
@ApiModel(value = "Devices对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("devices")
public class Devices implements Serializable, TypeConverter<Devices, DevicesDTO> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField
    private Integer agentId;

    @TableField
    private String cpu;

    @TableField
    private String imgUrl;

    @TableField
    private String manufacturer;

    @TableField
    private String model;

    @TableField
    private String name;

    @TableField
    private String password;

    @TableField
    private Integer platform;

    @TableField
    private String size;

    @TableField
    private String status;

    @TableField
    private String udId;

    @TableField
    private String version;

    @TableField
    private String nickName;

    @TableField
    private String user;

    @TableField
    String chiName;

    @TableField
    Integer temperature;

    @TableField
    Integer level;

    public static Devices newDeletedDevice(int id) {
        String tips = "Device does not exist.";
        return new Devices()
                .setAgentId(0)
                .setStatus("DISCONNECTED")
                .setPlatform(0)
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
                .setTemperature(0)
                .setLevel(0);
    }
}
