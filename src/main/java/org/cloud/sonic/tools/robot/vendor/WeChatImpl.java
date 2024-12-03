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
 * @des 企业微信机器人推送实作类
 * @date 2022/12/20
 */
@Slf4j
@Service("WeChatImpl")
public class WeChatImpl implements RobotMessenger {

    Expression templateTestSuiteMessage = RobotMessenger.parseTemplate("""
            **测试套件: #{suiteName} 运行完毕！**
            通过数：#{pass}
            异常数：#{warn}
            失败数：#{fail}
            测试报告：[点击查看](#{url})""");
    Expression templateProjectSummaryMessage = RobotMessenger.parseTemplate("""
            ### Sonic云真机测试平台#{isWeekly ? '周': '日'}报
            > ###### 项目：#{projectName}
            > ###### 时间：#{getFormat().format(startDate)} ～ #{getFormat().format(endDate)}
            > ###### 共测试：#{total}次
            > ###### 通过数：<font color="info">#{pass}</font>
            > ###### 异常数：<font color="#{warn == 0 ? 'info' : 'warning'}">#{warn}</font>
            > ###### 失败数：<font color="#{fail == 0 ? 'info' : 'warning'}">#{fail}</font>
            > ###### 测试通过率：#{rate}%
            > ###### 详细统计：[点击查看](#{url})""");
    Expression templateDeviceMessage = RobotMessenger.parseTemplate("""
            ### Sonic设备高温#{errorType == 1 ? '预警' : '超时，已关机！'}
            > ###### 设备序列号：#{udId}
            > ###### 电池温度：<font color="warning">#{tem}℃</font>""");

    /**
     * @param restTemplate RestTemplate
     * @param token        机器人token
     * @param content      通知内容
     * @author ZhouYiXun
     * @des 企业微信机器人传送讯息方法
     * @date 2021/8/20 18:20
     */
    private void signAndSend(RestTemplate restTemplate, String token, String content) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msgtype", "markdown");
            JSONObject markdown = new JSONObject();
            markdown.put("content", content);
            jsonObject.put("markdown", markdown);
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
