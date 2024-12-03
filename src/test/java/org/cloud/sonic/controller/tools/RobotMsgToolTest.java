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
package org.cloud.sonic.controller.tools;

import org.cloud.sonic.controller.ControllerApplication;
import org.cloud.sonic.controller.tools.robot.Message;
import org.cloud.sonic.controller.tools.robot.RobotMessenger;
import org.cloud.sonic.controller.tools.robot.message.DeviceMessage;
import org.cloud.sonic.controller.tools.robot.message.ProjectSummaryMessage;
import org.cloud.sonic.controller.tools.robot.message.TestSuiteMessage;
import org.cloud.sonic.controller.tools.robot.vendor.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Date;

@SpringBootTest(classes = ControllerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class RobotMsgToolTest {

    private void testMessage(RobotMessenger bot, String token, String secret) {
        for (var msg : new Message[]{new DeviceMessage(0, new BigDecimal(0), "test"), new ProjectSummaryMessage(0, "test", new Date(System.currentTimeMillis() - 7 * 24 * 3600 * 1000), new Date(), 1, 3, 4, 1.4, 3, "https://sonic/?#=+", true), new ProjectSummaryMessage(0, "test", new Date(System.currentTimeMillis() - 24 * 3600 * 1000), new Date(), 1, 3, 4, 1.4, 3, "https://sonic/?#=+", false), new TestSuiteMessage("asf", 0, 1, 4, 5, 3, "https://sonic/?#=+"),}) {
            bot.sendMessage(new RestTemplate(), token, secret, "", msg);
        }
    }

    @Test
    public void testWechat() {
        String wechatToken = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=f233d0b8-8ced-43a1-97c9-84f2d0acbd94";
        testMessage(new WeChatImpl(), wechatToken, "");
    }

    @Test
    public void testSlack() {
        String slackWebhook = "https://hooks.slack.com/services/TAMNTVCAH/B04PURURLEM/XXXXXXXXX";
        testMessage(new SlackNotifyImpl(), slackWebhook, "");
    }

    @Test
    public void testFeishu() {
        String feishuToken = "https://open.feishu.cn/open-apis/bot/v2/hook/xxx";
        testMessage(new FeiShuImpl(), feishuToken, "");
    }

    @Test
    public void testTelegram() {
        String telegramToken = "https://api.telegram.org/botxxx/sendMessage?chat_id=zzz";
        testMessage(new TelegramImpl(), telegramToken, "");
    }

    @Test
    public void testLine() {
        String lineToken = "";
        testMessage(new LineNotifyImpl(), lineToken, "");
    }

}
