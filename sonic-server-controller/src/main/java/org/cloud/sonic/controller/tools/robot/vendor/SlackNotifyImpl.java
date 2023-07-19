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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


/**
 * @author young(stephenwang1011)
 * @des Slack robot消息推送
 * @date 2023/2/10
 */
@Slf4j
@Service("SlackNotifyImpl")
public class SlackNotifyImpl implements RobotMessenger {

    Expression templateTestSuiteMessage = RobotMessenger.parseTemplate("""
            #{
            {
              text: '测试套件:' + suiteName + '运行完毕！',
              blocks: {
                { type: 'section',
                  text: {
                    type: 'mrkdwn',
                    text: '*测试套件:' + suiteName + '运行完毕！*
            通过数：'+pass+'
            异常数：'+warn+'
            失败数：'+fail+'
            测试报告：'+url
                  }
                }
              }
            }
            }""");
    Expression templateProjectSummaryMessage = RobotMessenger.parseTemplate("""
            #{
            {
              text: 'Sonic云真机测试平台'+(isWeekly?'周':'日')+'报',
              blocks: {
                { type: 'section',
                  text: {
                    type: 'mrkdwn',
                    text: '*Sonic云真机测试平台'+(isWeekly ? '周': '日')+'报*
            项目：'+projectName+'
            时间：'+getFormat().format(startDate)+' ～ '+getFormat().format(endDate)+'
            共测试：'+total+'次
            通过数：'+pass+'
            异常数：'+warn+'
            失败数：'+fail+'
            测试通过率：'+rate+'%
            详细统计：'+url
                  }
                }
              }
            }
            }""");
    Expression templateDeviceMessage = RobotMessenger.parseTemplate("""
            #{
            {
              text: '设备温度异常通知',
              blocks: {
                { type: 'section',
                  text: {
                    type: 'mrkdwn',
                    text: '*Sonic设备高温'+(errorType == 1 ? '预警' : '超时，已关机！')+'*
            设备序列号：'+udId+'
            电池温度：'+tem+'℃'
                  }
                }
              }
            }
            }""");

    /**
     * @param restTemplate RestTemplate
     * @param token        机器人token
     * @param secret       机器人密钥
     * @param content      通知内容
     * @author young(stephenwang1011)
     * @des SLACK发送测试通知
     * @date 2023/2/14
     */
    private void signAndSend(RestTemplate restTemplate, String token, String secret, Object content) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(token, content, String.class);
            log.info("robot result: " + responseEntity.getBody());
        } catch (Exception e) {
            log.warn("robot send failed, cause: " + e.getMessage());
        }
    }

    @Override
    public void sendMessage(RestTemplate restTemplate, String token, String secret, Expression messageTemplate, Message msg) {
        Map<?, ?> content = messageTemplate.getValue(ctx, msg, Map.class);
        signAndSend(restTemplate, token, secret, content);
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