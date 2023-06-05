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

import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.controller.tools.robot.Message;
import org.cloud.sonic.controller.tools.robot.RobotMessenger;
import org.springframework.expression.Expression;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author ayumi760405
 * @des Line Notify 推送实作类，可以参考 https://notify-bot.line.me/doc/en/
 * @date 2022/12/29
 */
@Slf4j
@Service("LineNotifyImpl")
public class LineNotifyImpl implements RobotMessenger {

    //line notify的host
    private static final String LINE_NOTIFY_HOST = "https://notify-api.line.me/api/notify";

    Expression templateTestSuiteMessage = RobotMessenger.parseTemplate("""
            *Sonic云真机测试报告*
            测试套件: #{suiteName} 运行完毕！
            通过数：#{pass}
            异常数：#{warn>0?' `'+warn+'`':warn}
            失败数：#{fail>0?' `'+fail+'`':fail}
            测试报告: #{url}
            """);
    Expression templateProjectSummaryMessage = RobotMessenger.parseTemplate("""
            *Sonic云真机测试平台#{isWeekly ? '周': '日'}报*
            项目：#{projectName}
            时间：#{getFormat().format(startDate)} ～ #{getFormat().format(endDate)}
            共测试：#{total}次
            通过数：#{pass}
            异常数：#{warn>0?' `'+warn+'`':warn}
            失败数：#{fail>0?' `'+fail+'`':fail}
            测试通过率：#{rate}%
            详细统计：#{url}
            """);
    Expression templateDeviceMessage = RobotMessenger.parseTemplate("""
            *设备温度异常通知*
            Sonic设备高温#{errorType == 1 ? '预警' : '超时，已关机！'}
            设备序列号：#{udId}
            电池温度：#{tem}℃""");

    /**
     * @param restTemplate RestTemplate
     * @param token        机器人token
     * @param message      通知内容
     * @author ayumi760405
     * @des Line Notify 传送讯息方法
     * @date 2022/12/29
     */
    private void signAndSend(RestTemplate restTemplate, String token, String message) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<String, String>();
            requestMap.add("message", message);
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(requestMap, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(LINE_NOTIFY_HOST, entity, String.class);
            log.info("robot result: " + responseEntity.getBody());
        } catch (Exception e) {
            log.warn("robot send failed, cause: " + e.getMessage());
        }
    }

    @Override
    public void sendMessage(RestTemplate restTemplate, String token, String secret, Expression messageTemplate, Message msg) {
        String content = messageTemplate.getValue(ctx, msg, String.class);
        signAndSend(restTemplate, token, content);
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
