package org.cloud.sonic.controller.tools.robot.vendor;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.controller.tools.robot.RobotMessenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author ayumi760405
 * @des 企业微信机器人推送实作类
 * @date 2022/12/20
 */
@Component("WeChatImpl")
public class WeChatImpl implements RobotMessenger {

    private final Logger logger = LoggerFactory.getLogger(DingTalkImpl.class);

    //从配置文件获取前端部署的host
    @Value("${robot.client.host}")
    private String clientHost;

    /**
     * @param restTemplate RestTemplate
     * @param token      机器人token
     * @param secret     机器人密钥
     * @param jsonObject 通知内容
     * @author ZhouYiXun
     * @des 企业微信机器人传送讯息方法
     * @date 2021/8/20 18:20
     */
    private void signAndSend(RestTemplate restTemplate, String token, String secret, JSONObject jsonObject) {
        clientHost = clientHost.replace(":80/", "/");
        try {
            ResponseEntity<JSONObject> responseEntity =
                    restTemplate.postForEntity(token, jsonObject, JSONObject.class);
            logger.info("robot result: " + responseEntity.getBody());
        } catch (Exception e) {
            logger.warn("robot send failed, cause: " + e.getMessage());
        }
    }

    /**
     * @param restTemplate RestTemplate
     * @param token     机器人token
     * @param secret    机器人密钥
     * @param suiteName 套件名称
     * @param pass      通过数量
     * @param warn      警告数量
     * @param fail      失败数量
     * @param projectId 项目id
     * @param resultId  结果id
     * @return void
     * @author ZhouYiXun
     * @des 发送每次测试结果到企业微信
     * @date 2021/8/20 18:29
     */
    @Override
    public void sendResultFinishReport(RestTemplate restTemplate, String token, String secret, String suiteName, int pass, int warn, int fail, int projectId, int resultId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", "**测试套件: " + suiteName + " 运行完毕！**\n" +
                "通过数：" + pass + " \n" +
                "异常数：" + warn + " \n" +
                "失败数：" + fail + "\n" +
                "测试报告：[点击查看](" + clientHost + "/Home/" + projectId + "/ResultDetail/" + resultId + ")");
        jsonObject.put("markdown", markdown);
        this.signAndSend(restTemplate, token, secret, jsonObject);
    }

    /**
     * @param restTemplate RestTemplate
     * @param token       机器人token
     * @param secret      机器人密钥
     * @param projectId   项目id
     * @param projectName 项目名称
     * @param yesterday   昨天的起始时间
     * @param today       今天的起始时间
     * @return void
     * @author ZhouYiXun
     * @des 发送日报
     * @date 2021/8/20 18:42
     */
    @Override
    public void sendDayReportMessage(RestTemplate restTemplate, String token, String secret, int projectId, String projectName, String yesterday, String today, int passCount, int warnCount, int failCount) {

        JSONObject jsonObject = new JSONObject();
        int total = passCount + warnCount + failCount;
        String warnColorString;

        if (warnCount == 0) {
            warnColorString = "<font color=\"info\">" + warnCount + "</font>";
        } else {
            warnColorString = "<font color=\"warning\">" + warnCount + "</font>";
        }

        String failColorString;

        if (failCount == 0) {
            failColorString = "<font color=\"info\">" + failCount + "</font>";
        } else {
            failColorString = "<font color=\"warning\">" + failCount + "</font>";
        }

        jsonObject.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", "### Sonic云真机测试平台日报 \n" +
                "> ###### 项目：" + projectName + " \n" +
                "> ###### 时间：" + yesterday + " ～ " + today + " \n" +
                "> ###### 通过数：<font color=\"info\">" + passCount + "</font> \n" +
                "> ###### 异常数：" + warnColorString + " \n" +
                "> ###### 失败数：" + failColorString + " \n" +
                "> ###### 测试通过率：" + (total > 0 ?
                new BigDecimal(((float) passCount / total) * 100).setScale(2, RoundingMode.HALF_UP).doubleValue() : 0) + "% \n" +
                "> ###### 详细统计：[点击查看](" + clientHost + "/Home/" + projectId + ")");
        jsonObject.put("markdown", markdown);
        this.signAndSend(restTemplate, token, secret, jsonObject);
    }

    /**
     * @param restTemplate RestTemplate
     * @param token       机器人token
     * @param secret      机器人密钥
     * @param errorType   errorType
     * @param tem         温度
     * @param udId        设备Id
     * @return void
     * @author ZhouYiXun
     * @des 发送设备错误讯息
     * @date 2021/8/20 18:42
     */
    @Override
    public void sendErrorDevice(RestTemplate restTemplate, String token, String secret, int errorType, int tem, String udId) {
        JSONObject jsonObject = new JSONObject();
        JSONObject markdown = new JSONObject();
        if (errorType == 1) {
            markdown.put("content", "### Sonic设备高温预警 \n" +
                    "> ###### 设备序列号：" + udId + " \n" +
                    "> ###### 电池温度：<font color=\"warning\">" + (tem / 10) + " ℃</font>");
        } else {
            markdown.put("content", "### Sonic设备高温超时，已关机！ \n" +
                    "> ###### 设备序列号：" + udId + " \n" +
                    "> ###### 电池温度：<font color=\"warning\">" + (tem / 10) + " ℃</font>");
        }
        jsonObject.put("msgtype", "markdown");
        jsonObject.put("markdown", markdown);
        this.signAndSend(restTemplate, token, secret, jsonObject);
    }

    /**
     * @param restTemplate RestTemplate
     * @param token       机器人token
     * @param secret      机器人密钥
     * @param projectId   项目id
     * @param projectName 项目名称
     * @param yesterday   昨天的起始时间
     * @param today       今天的起始时间
     * @param passCount   通过数量
     * @param warnCount   警告数量
     * @param failCount   失败数量
     * @param count       测试数量
     * @return void
     * @author ZhouYiXun
     * @des 发送周报
     * @date 2021/8/20 18:42
     */
    @Override
    public void sendWeekReportMessage(RestTemplate restTemplate, String token, String secret, int projectId, String projectName, String yesterday, String today, int passCount, int warnCount, int failCount, int count) {
        JSONObject jsonObject = new JSONObject();
        int total = passCount + warnCount + failCount;
        String warnColorString;
        if (warnCount == 0) {
            warnColorString = "<font color=\"info\">" + warnCount + "</font>";
        } else {
            warnColorString = "<font color=\"warning\">" + warnCount + "</font>";
        }
        String failColorString;
        if (failCount == 0) {
            failColorString = "<font color=\"info\">" + failCount + "</font>";
        } else {
            failColorString = "<font color=\"warning\">" + failCount + "</font>";
        }
        jsonObject.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", "### Sonic云真机测试平台周报 \n" +
                "> ###### 项目：" + projectName + " \n" +
                "> ###### 时间：" + yesterday + " ～ " + today + " \n" +
                "> ###### 共测试：" + count + " 次\n" +
                "> ###### 通过数：<font color=\"info\">" + passCount + "</font> \n" +
                "> ###### 异常数：" + warnColorString + " \n" +
                "> ###### 失败数：" + failColorString + " \n" +
                "> ###### 测试通过率：" + (total > 0 ?
                new BigDecimal(((float) passCount / total) * 100).setScale(2, RoundingMode.HALF_UP).doubleValue() : 0) + "% \n" +
                "> ###### 详细统计：[点击查看](" + clientHost + "/Home/" + projectId + ")");
        jsonObject.put("markdown", markdown);
        this.signAndSend(restTemplate, token, secret, jsonObject);
    }
}
