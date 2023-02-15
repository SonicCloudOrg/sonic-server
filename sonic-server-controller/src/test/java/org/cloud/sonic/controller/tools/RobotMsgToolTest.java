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
import org.cloud.sonic.controller.models.interfaces.RobotType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = ControllerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class RobotMsgToolTest {

    @Autowired
    private RobotMsgTool robotMsgTool;

    private String wechatToken = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=xxx";

    private String feishuToken = "https://open.feishu.cn/open-apis/bot/v2/hook/xxx";

    // API path https://api.telegram.org/bot<token>/sendMessage?chat_id=<chat_id>
    private String telegramToken = "https://api.telegram.org/botxxx/sendMessage?chat_id=zzz";

    private String lineToken = "XXXXX";

    private String slackWebhook = "https://hooks.slack.com/services/TAMNTVCAH/B04PURURLEM/XXXXXXXXX";

    @Test
    public void sendDayReportMessage() {
        robotMsgTool.sendDayReportMessage(wechatToken, "", 1, "2",
                "1", "2", 1, 2, 3, RobotType.WeChat);
    }

    @Test
    public void sendSlackDayReportMessage() {
        robotMsgTool.sendDayReportMessage(slackWebhook, "", 1, "Operator App",
                "2023-2-14 14:52:01", "2023-2-14 14:59:21", 40, 0, 0, RobotType.SlackNotify);
    }


    @Test
    public void sendDayReportMessage2() {
        robotMsgTool.sendDayReportMessage(wechatToken, "", 1, "2",
                "1", "2", 1, 0, 0, RobotType.WeChat);
    }

    @Test
    public void testSendErrorDeviceSlack() {
        robotMsgTool.sendErrorDevice(slackWebhook, "", RobotType.SlackNotify, 0, 80, "111");
    }

    @Test
    public void testSendErrorDeviceSlack2() {
        robotMsgTool.sendErrorDevice(slackWebhook, "", RobotType.SlackNotify, 1, 80, "111");
    }


    @Test
    public void testSendErrorDevice2() {
        robotMsgTool.sendErrorDevice(wechatToken, "", RobotType.WeChat, 2, 80, "111");
    }

    @Test
    public void testSendErrorDeviceFeishu() {
        robotMsgTool.sendErrorDevice(feishuToken, "", RobotType.FeiShu, 1, 80, "测试");
    }

    @Test
    public void testSendErrorDeviceFeishu2() {
        robotMsgTool.sendErrorDevice(feishuToken, "", RobotType.FeiShu, 2, 80, "测试");
    }


    @Test
    public void sendWeekReportMessage() {
        robotMsgTool.sendWeekReportMessage(wechatToken, "", 1, "2",
                "1", "2", 1, 0, 0, 100, RobotType.WeChat);
    }

    @Test
    public void sendWeekReportMessageSlack() {
        robotMsgTool.sendWeekReportMessage(slackWebhook, "", 1, "2",
                "1", "2", 1, 0, 0, 100, RobotType.SlackNotify);
    }

    @Test
    public void sendResultFinishReport() {
        robotMsgTool.sendResultFinishReport(wechatToken, "", "111", 1, 1, 1, 1,
                1, RobotType.WeChat);
    }

    @Test
    public void sendResultFinishReportSlack() {
        robotMsgTool.sendResultFinishReport(slackWebhook, "", "suite-01", 1, 1, 1, 1,
                1, RobotType.SlackNotify);
    }

    @Test
    public void sendDayReportMessageForTelegram() {
        robotMsgTool.sendDayReportMessage(telegramToken, "", 1, "2",
                "1", "2", 1, 2, 3, RobotType.Telegram);
    }

    @Test
    public void sendResultFinishReportForTelegram() {
        robotMsgTool.sendResultFinishReport(telegramToken, "", "111", 1, 1, 1, 1,
                1, RobotType.Telegram);
    }

    @Test
    public void sendWeekReportMessageForTelegram() {
        robotMsgTool.sendWeekReportMessage(telegramToken, "", 1, "2",
                "1", "2", 1, 0, 0, 100, RobotType.Telegram);
    }

    @Test
    public void testSendErrorDeviceForTelegram() {
        robotMsgTool.sendErrorDevice(telegramToken, "", RobotType.Telegram, 2, 80, "测试");
    }

    @Test
    public void sendDayReportMessageForLineNotify() {
        robotMsgTool.sendDayReportMessage(lineToken, "", 1, "2",
                "1", "2", 1, 2, 3, RobotType.LineNotify);
    }

    @Test
    public void sendResultFinishReportForLineNotify() {
        robotMsgTool.sendResultFinishReport(lineToken, "", "111", 1, 1, 2, 1,
                1, RobotType.LineNotify);
    }

    @Test
    public void sendWeekReportMessageForLineNotify() {
        robotMsgTool.sendWeekReportMessage(lineToken, "", 1, "2",
                "1", "2", 1, 3, 5, 100, RobotType.LineNotify);
    }

    @Test
    public void testSendErrorDeviceForLineNotify() {
        robotMsgTool.sendErrorDevice(lineToken, "", RobotType.LineNotify, 2, 80, "测试");
    }

}
