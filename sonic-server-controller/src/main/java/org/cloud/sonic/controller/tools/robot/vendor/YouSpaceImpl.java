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

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * @author ayumi760405
 * @des 友空间机器人推送实作类
 * @date 2022/12/20
 */
@Slf4j
@Service("YouSpaceImpl")
public class YouSpaceImpl implements RobotMessenger {

    Expression templateTestSuiteMessage = RobotMessenger.parseTemplate("""
            #{
            {
              businessId: '测试套件: ' + suiteName + '运行完毕！',
              titleZone: {type: 0, text: '测试套件: ' + suiteName + '运行完毕！'},
              contentZone: {
                {type: 'textView', data: { text: '通过数：' + pass, level: 1}},
                {type: 'textView', data: { text: '异常数：' + warn, level: 1}},
                {type: 'textView', data: { text: '失败数：' + fail, level: 1}},
                {type: 'buttonView', data: {mode: 0, text: '点击查看测试报告', url: url}}
              }
            }
            }""");
    Expression templateProjectSummaryMessage = RobotMessenger.parseTemplate("""
            #{
            {
              businessId: 'Sonic云真机测试平台'+(isWeekly?'周':'日')+'报',
              titleZone: {type: 0, text: 'Sonic云真机测试平台'+(isWeekly?'周':'日')+'报'},
              contentZone: {
                {type: 'textView', data: {text: '项目：' + projectName, level: 1}},
                {type: 'textView', data: {text: '时间：' + getFormat().format(startDate) + ' ～ ' + getFormat().format(endDate), level: 1}},
                {type: 'textView', data: {text: '共测试：' + total, level: 1}},
                {type: 'textView', data: {text: '通过数：' + pass, level: 1}},
                {type: 'textView', data: {text: '异常数：' + warn, level: 1}},
                {type: 'textView', data: {text: '失败数：' + fail, level: 1}},
                {type: 'textView', data: {text: '测试通过率：' + rate, level: 1}},
                {type: 'buttonView', data: {mode: 0, text: '点击查看', url: url}}
              }
            }
            }""");
    Expression templateDeviceMessage = RobotMessenger.parseTemplate("""
            #{
            {
              businessId: 'Sonic设备高温'+(errorType==1?'预警':'超时，已关机！'),
              titleZone: {type: 0, text: 'Sonic设备高温'+(errorType==1?'预警':'超时，已关机！')},
              contentZone: {
                {type: 'textView', data: {text: '设备序列号：' + udId, level: 1}},
                {type: 'textView', data: {text: '电池温度：' + tem + ' ℃', level: 1}}
              }
            }
            }""");

    /**
     * @param restTemplate RestTemplate
     * @param token        机器人token
     * @param jsonObject   通知内容
     * @author ZhouYiXun
     * @des 友空间签名方法
     * @date 2021/8/20 18:20
     */
    private void signAndSend(RestTemplate restTemplate, String token, Map<?, ?> jsonObject) {
        try {
            JSONObject you = new JSONObject();
            you.put("timestamp", System.currentTimeMillis());
            String encoded_content = Base64.getEncoder().encodeToString(JSONObject.toJSONString(jsonObject).getBytes(StandardCharsets.UTF_8));
            you.put("content", encoded_content);
            ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(token, you, JSONObject.class);
            log.info("robot result: " + responseEntity.getBody());
        } catch (Exception e) {
            log.warn("robot send failed, cause: " + e.getMessage());
        }
    }

    @Override
    public void sendMessage(RestTemplate restTemplate, String token, String secret, Expression messageTemplate, Message msg) {
        Map<?, ?> json = messageTemplate.getValue(ctx, msg, Map.class);
        this.signAndSend(restTemplate, token, json);
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
