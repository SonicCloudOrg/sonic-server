//package org.cloud.sonic.controller.tools;
//
//import org.cloud.sonic.controller.BaseUnit;
//import org.cloud.sonic.controller.models.interfaces.RobotType;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//public class RobotTest extends BaseUnit {
//
//    @Autowired
//    RobotMsgTool robotMsgTool;
//
//    private String wechatToken = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=xxx";
//
//    private String feishuToken = "https://open.feishu.cn/open-apis/bot/v2/hook/xxx";
//
//    // API path https://api.telegram.org/bot<token>/sendMessage?chat_id=<chat_id>
//    private String telegramToken = "https://api.telegram.org/botxxx/sendMessage?chat_id=zzz";

//    @Test
//    public void sendDayReportMessage() {
//        robotMsgTool.sendDayReportMessage(wechatToken, "" ,1, "2",
//                "1", "2" , 1,2,3, RobotType.WeChat);
//    }
//
//    @Test
//    public void sendDayReportMessage2() {
//        robotMsgTool.sendDayReportMessage(wechatToken, "" ,1, "2",
//                "1", "2" , 1,0,0, RobotType.WeChat);
//    }
//
//    @Test
//    public void testSendErrorDevice1() {
//        robotMsgTool.sendErrorDevice(wechatToken, "", RobotType.WeChat, 1, 80,"111");
//    }
//
//
//    @Test
//    public void testSendErrorDevice2() {
//        robotMsgTool.sendErrorDevice(wechatToken, "", RobotType.WeChat, 2, 80,"111");
//    }
//
//    @Test
//    public void testSendErrorDeviceFeishu() {
//        robotMsgTool.sendErrorDevice(feishuToken, "", RobotType.FeiShu, 1, 80,"测试");
//    }
//
//    @Test
//    public void testSendErrorDeviceFeishu2() {
//        robotMsgTool.sendErrorDevice(feishuToken, "", RobotType.FeiShu, 2, 80,"测试");
//    }
//
//
//    @Test
//    public void sendWeekReportMessage() {
//        robotMsgTool.sendWeekReportMessage(wechatToken, "" ,1, "2",
//                "1", "2" , 1,0,0,100, RobotType.WeChat);
//    }
//
//    @Test
//    public void sendResultFinishReport() {
//        robotMsgTool.sendResultFinishReport(wechatToken, "","111", 1,1,1,1,
//                1,RobotType.WeChat);
//    }
//
//    @Test
//    public void sendDayReportMessageForTelegram() {
//        robotMsgTool.sendDayReportMessage(telegramToken, "" ,1, "2",
//                "1", "2" , 1,2,3, RobotType.Telegram);
//    }
//
//    @Test
//    public void sendResultFinishReportForTelegram() {
//        robotMsgTool.sendResultFinishReport(telegramToken, "","111", 1,1,1,1,
//                1,RobotType.Telegram);
//    }
//
//    @Test
//    public void sendWeekReportMessageForTelegram() {
//        robotMsgTool.sendWeekReportMessage(telegramToken, "" ,1, "2",
//                "1", "2" , 1,0,0,100, RobotType.Telegram);
//    }
//
//    @Test
//    public void testSendErrorDeviceForTelegram() {
//        robotMsgTool.sendErrorDevice(telegramToken, "", RobotType.Telegram, 2, 80,"测试");
//    }
//
//}
