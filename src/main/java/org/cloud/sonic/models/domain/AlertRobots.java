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

import com.baomidou.mybatisplus.annotation.*;
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
import org.cloud.sonic.controller.models.dto.AlertRobotsDTO;

import java.io.Serializable;


@Schema(name = "AlertRobots对象", description = "")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("alert_robots")
@TableComment("alert_robots表")
@TableCharset(MySqlCharsetConstant.DEFAULT)
@TableEngine(MySqlEngineConstant.InnoDB)
public class AlertRobots implements Serializable, TypeConverter<AlertRobots, AlertRobotsDTO> {

    @TableId(value = "id", type = IdType.AUTO)
    @IsAutoIncrement
    private Integer id;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @Column(value = "project_id", comment = "可用项目id， null为公共机器人")
    @Index(value = "IDX_PROJECT_ID", columns = {"project_id"})
    private Integer projectId;

    @TableField
    @Column(isNull = false, comment = "显示名称")
    private String name;

    @TableField
    @Column(value = "robot_secret", comment = "机器人秘钥")
    private String robotSecret;

    @TableField
    @Column(value = "robot_token", isNull = false, comment = "机器人token/接口uri")
    private String robotToken;

    @TableField
    @Column(value = "robot_type", isNull = false, comment = "机器人类型")
    private Integer robotType;

    @TableField
    @Column(value = "scene", isNull = false, comment = "使用场景，可选 agent, testsuite, summary")
    private String scene;

    @Column(value = "mute_rule", length = 1024, isNull = false, defaultValue = "", comment = "静默规则，SpEL表达式，表达式求值为true时不发送消息，否则正常发送")
    private String muteRule;

    @Column(value = "template", length = 4096, isNull = false, defaultValue = "", comment = "通知模板，SpEL表达式，表达式为空时机器人类型自动使用默认值")
    private String template;
}
