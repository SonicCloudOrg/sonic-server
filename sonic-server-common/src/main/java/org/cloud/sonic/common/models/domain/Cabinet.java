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
package org.cloud.sonic.common.models.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlCharsetConstant;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlEngineConstant;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.common.models.base.TypeConverter;
import org.cloud.sonic.common.models.dto.CabinetDTO;

import java.io.Serializable;

@ApiModel(value = "机柜对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("cabinets")
@TableComment("cabinets表")
@TableCharset(MySqlCharsetConstant.DEFAULT)
@TableEngine(MySqlEngineConstant.InnoDB)
public class Cabinet implements Serializable, TypeConverter<Cabinet, CabinetDTO> {
    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    private Integer id;

    @TableField
    @Column(isNull = false, comment = "size")
    private Integer size;

    @TableField
    @Column(isNull = false, comment = "name")
    private String name;

    @TableField
    @Column(value = "secret_key", isNull = false, comment = "机柜密钥")
    private String secretKey;

    @TableField
    @Column(value = "low_level", isNull = false, comment = "lowLevel", defaultValue = "40")
    private Integer lowLevel;

    @TableField
    @Column(value = "low_gear", isNull = false, comment = "lowGear", defaultValue = "1")
    private Integer lowGear;

    @TableField
    @Column(value = "high_level", isNull = false, comment = "highLevel", defaultValue = "90")
    private Integer highLevel;

    @TableField
    @Column(value = "high_gear", isNull = false, comment = "highGear", defaultValue = "14")
    private Integer highGear;

    @TableField
    @Column(value = "high_temp", isNull = false, comment = "highTemp", defaultValue = "45")
    private Integer highTemp;

    @TableField
    @Column(value = "high_temp_time", isNull = false, comment = "highTempTime", defaultValue = "15")
    private Integer highTempTime;

    @TableField
    @Column(value = "robot_secret", isNull = false, comment = "机器人秘钥")
    private String robotSecret;

    @TableField
    @Column(value = "robot_token", isNull = false, comment = "机器人token")
    private String robotToken;

    @TableField
    @Column(value = "robot_type", isNull = false, comment = "机器人类型", defaultValue = "1")
    private Integer robotType;
}
