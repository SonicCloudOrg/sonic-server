package org.cloud.sonic.controller.tools.robot.vendor;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.controller.tools.robot.RobotMessenger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author ayumi760405
 * @des Telegram电报机器人推送实作类，可以参考 https://core.telegram.org/bots/api#sendmessage
 * @date 2022/12/20
 */
@Slf4j
@Component("TelegramImpl")
public class TelegramImpl implements RobotMessenger {

    //从配置文件获取前端部署的host
    @Value("${robot.client.host}")
    private String clientHost;

    /**
     * @param restTemplate RestTemplate
     * @param token      机器人token
     * @param secret     机器人密钥
     * @param jsonObject 通知内容
     * @author ayumi760405
     * @des Telegram电报机器人传送讯息方法
     * @date 2022/12/20
     */
    private void signAndSend(RestTemplate restTemplate, String token, String secret, JSONObject jsonObject) {
        clientHost = clientHost.replace(":80/", "/");
        try {
            ResponseEntity<JSONObject> responseEntity =
                    restTemplate.postForEntity(token, jsonObject, JSONObject.class);
            log.info("robot result: " + responseEntity.getBody());
        } catch (Exception e) {
            log.warn("robot send failed, cause: " + e.getMessage());
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
     * @author ayumi760405
     * @des 发送每次测试结果到Telegram
     * @date 2022/12/20
     */
    @Override
    public void sendResultFinishReport(RestTemplate restTemplate, String token, String secret, String suiteName, int pass, int warn, int fail, int projectId, int resultId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("parse_mode", "html");
        String reportLink = clientHost + "/Home/" + projectId + "/ResultDetail/" + resultId;
        jsonObject.put("text",
                "<strong>测试套件: " + suiteName + " 运行完毕！</strong>\n" +
                        "<i>通过数：" + pass + "</i>\n" +
                        "<i>异常数：" + warn + "</i>\n" +
                        "<i>失败数：" + fail + "</i>\n" +
                        "<b>测试报告：<a href=\"" + reportLink +"\">" + reportLink + "</a></b>\n");
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
     * @author ayumi760405
     * @des 发送日报
     * @date 2022/12/20
     */
    @Override
    public void sendDayReportMessage(RestTemplate restTemplate, String token, String secret, int projectId, String projectName, String yesterday, String today, int passCount, int warnCount, int failCount) {
        JSONObject jsonObject = new JSONObject();
        int total = passCount + warnCount + failCount;
        String warnColorString;
        if (warnCount == 0) {
            warnColorString = "<b>" + warnCount + "</b>";
        } else {
            warnColorString = "<b>" + warnCount + "</b>";
        }
        String failColorString;
        if (failCount == 0) {
            failColorString = "<b>" + failCount + "</b>";
        } else {
            failColorString = "<b>" + failCount + "</b>";
        }
        jsonObject.put("parse_mode", "html");
        String reportLink = clientHost + "/Home/" + projectId;
        jsonObject.put("text", "<strong> Sonic云真机测试平台日报 </strong>\n" +
                "<i>项目：" + projectName + "</i>\n" +
                "<i>时间：" + yesterday + " ～ " + today + "</i>\n" +
                "<i>通过数：" + passCount + "</i>\n" +
                "<i>异常数：" + warnColorString + "</i>\n" +
                "<i>失败数：" + failColorString + "</i>\n" +
                "<i> 测试通过率：" + (total > 0 ?
                new BigDecimal(((float) passCount / total) * 100).setScale(2, RoundingMode.HALF_UP).doubleValue() : 0) + "% </i>\n" +
                "<b>详细统计：<a href=\"" + reportLink +"\">" + reportLink + "</a></b>\n");
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
     * @author ayumi760405
     * @des 发送设备错误讯息
     * @date 2022/12/20
     */
    @Override
    public void sendErrorDevice(RestTemplate restTemplate, String token, String secret, int errorType, int tem, String udId) {
        JSONObject jsonObject = new JSONObject();
        if (errorType == 1) {
            jsonObject.put("text", "<strong> Sonic设备高温预警 </strong>\n" +
                    "<i> 设备序列号：" + udId + "</i> \n" +
                    "<i> 电池温度：" + (tem / 10) + " ℃</i>");
        } else {
            jsonObject.put("text", "<strong> Sonic设备高温超时，已关机！ </strong>\n" +
                    "<i> 设备序列号：" + udId + "</i> \n" +
                    "<i> ###### 电池温度：" + (tem / 10) + " ℃</i>");
        }
        jsonObject.put("parse_mode", "html");
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
     * @author ayumi760405
     * @des 发送周报
     * @date 2022/12/20
     */
    @Override
    public void sendWeekReportMessage(RestTemplate restTemplate, String token, String secret, int projectId, String projectName, String yesterday, String today, int passCount, int warnCount, int failCount, int count) {
        JSONObject jsonObject = new JSONObject();
        int total = passCount + warnCount + failCount;
        String reportLink = clientHost + "/Home/" + projectId;
        jsonObject.put("parse_mode", "html");
        jsonObject.put("text", "<strong> Sonic云真机测试平台周报 </strong>\n" +
                "<i> 项目：" + projectName + "</i> \n" +
                "<i> 时间：" + yesterday + " ～ " + today + "</i> \n" +
                "<i> 共测试：" + count + " 次</i>\n" +
                "<i> 通过数：" + passCount + "</i> \n" +
                "<i> 异常数：" + warnCount + "</i> \n" +
                "<i> 失败数：" + failCount + "</i> \n" +
                "<i> 测试通过率：" + (total > 0 ?
                new BigDecimal(((float) passCount / total) * 100).setScale(2, RoundingMode.HALF_UP).doubleValue() : 0) + "% </i> \n" +
                "<b>详细统计：<a href=\"" + reportLink +"\">" + reportLink + "</a></b>\n");
        this.signAndSend(restTemplate, token, secret, jsonObject);
    }
}
