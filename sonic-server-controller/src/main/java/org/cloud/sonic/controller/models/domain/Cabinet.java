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
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.dto.CabinetDTO;

import java.io.Serializable;

@ApiModel(value = "机柜对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("cabinets")

public class Cabinet implements Serializable, TypeConverter<Cabinet, CabinetDTO> {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField
    private Integer size;

    @TableField
    private String name;

    @TableField
    private String secretKey;

    @TableField
    private Integer lowLevel;

    @TableField
    private Integer lowGear;

    @TableField
    private Integer highLevel;

    @TableField
    private Integer highGear;

    @TableField
    private Integer highTemp;

    @TableField
    private Integer highTempTime;

    @TableField
    private String robotSecret;

    @TableField
    private String robotToken;

    @TableField
    private Integer robotType;
}
