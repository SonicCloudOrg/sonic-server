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
package org.cloud.sonic.controller.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.AlertRobots;

import java.io.Serializable;

@Schema(name = "告警通知机器人DTO 模型")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertRobotsDTO implements Serializable, TypeConverter<AlertRobotsDTO, AlertRobots> {

    @Schema(description = "通知机器人id", example = "1")
    Integer id;
    @Schema(description = "机器人类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    Integer robotType;
    @Schema(description = "机器人token", requiredMode = Schema.RequiredMode.REQUIRED, example = "http://dingTalk.com?token=*****")
    String robotToken;
    @Schema(description = "机器人加签密钥", example = "qwe***")
    String robotSecret;
    @Schema(description = "机器人配置显示名称", example = "1")
    private String name;
    @Schema(description = "所属项目id，不属于任何项目的系统级机器人此项为null", example = "1")
    private Integer projectId;
    @Schema(description = "通知类型，可选 agent, testsuite, summary", example = "testsuite")
    private String scene;
    @Schema(description = "静默规则，SpEL表达式，表达式求值为真时不发送消息", example = "error == 0 && fail == 0")
    private String muteRule;
    @Schema(description = "通知模板，SpEL表达式，表达式为null时采用相应 机器人类型 的缺省模板", example = "alert! pass count is #{pass}, error count is #{error}")
    private String template;
}
