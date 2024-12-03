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
package org.cloud.sonic.controller.tools.robot.vendor;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.controller.tools.robot.Message;
import org.cloud.sonic.controller.tools.robot.RobotMessenger;
import org.springframework.expression.Expression;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author ayumi760405
 * @des Telegram电报机器人推送实作类，可以参考 https://core.telegram.org/bots/api#sendmessage
 * @date 2022/12/20
 */
@Slf4j
@Service("TelegramImpl")
public class TelegramImpl implements RobotMessenger {

    Expression templateTestSuiteMessage = RobotMessenger.parseTemplate("""
            <strong>测试套件: #{suiteName} 运行完毕！</strong>
            <i>通过数：#{pass}</i>
            <i>异常数：#{warn}</i>
            <i>失败数：#{fail}</i>
            <b>测试报告：<a href="#{url}">点击查看</a></b>""");
    Expression templateProjectSummaryMessage = RobotMessenger.parseTemplate("""
            <strong> Sonic云真机测试平台#{isWeekly ? '周': '日'}报</strong>
            <i> 项目：#{projectName}</i>
            <i> 时间：#{getFormat().format(startDate)} ～ #{getFormat().format(endDate)}</i>
            <i> 共测试：#{total}次</i>
            <i> 通过数：#{pass}</i>
            <i> 异常数：#{warn}</i>
            <i> 失败数：#{fail}</i>
            <i> 测试通过率：#{rate}%</i>
            <b>详细统计：<a href="#{url}">点击查看</a></b>""");
    Expression templateDeviceMessage = RobotMessenger.parseTemplate("""
            <strong> Sonic设备高温#{errorType == 1 ? '预警' : '超时，已关机！'}</strong>
            <i> 设备序列号：#{udId}</i>
            <i> 电池温度：#{tem}" ℃</i>""");

    /**
     * @param restTemplate RestTemplate
     * @param token        机器人token
     * @param content      通知内容
     * @author ayumi760405
     * @des Telegram电报机器人传送讯息方法
     * @date 2022/12/20
     */
    private void signAndSend(RestTemplate restTemplate, String token, String content) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("parse_mode", "html");
            jsonObject.put("text", content);
            ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(token, jsonObject, JSONObject.class);
            log.info("robot result: " + responseEntity.getBody());
        } catch (Exception e) {
            log.warn("robot send failed, cause: " + e.getMessage());
        }
    }

    @Override
    public void sendMessage(RestTemplate restTemplate, String token, String secret, Expression messageTemplate, Message msg) {
        String content = messageTemplate.getValue(ctx, msg, String.class);
        this.signAndSend(restTemplate, token, content);
    }

    @Override
    public Expression getDefaultTestSuiteTemplate() {
        return templateTestSuiteMessage;
    }

    @Override
    public Expression getDefaultProjectSummaryTemplate() {
        return templateProjectSummaryMessage;
    }

    @Override
    public Expression getDefaultDeviceMessageTemplate() {
        return templateDeviceMessage;
    }

}
