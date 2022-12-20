package org.cloud.sonic.controller.tools.robot.vendor;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.controller.tools.robot.RobotMessenger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ayumi760405
 * @des 飞书群机器人推送实作类
 * @date 2022/12/20
 */
@Slf4j
@Component("FeiShuImpl")
public class FeiShuImpl implements RobotMessenger {

    //从配置文件获取前端部署的host
    @Value("${robot.client.host}")
    private String clientHost;

    /**
     * @param restTemplate RestTemplate
     * @param token      机器人token
     * @param secret     机器人密钥
     * @param jsonObject 通知内容
     * @author ZhouYiXun
     * @des 飞书群机签名方法
     * @date 2021/8/20 18:20
     */
    private void signAndSend(RestTemplate restTemplate, String token, String secret, JSONObject jsonObject) {
        clientHost = clientHost.replace(":80/", "/");
        try {
            if (!StringUtils.isEmpty(secret)) {
                String timestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);
                String stringToSign = timestamp + "\n" + secret;
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(new SecretKeySpec(stringToSign.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
                byte[] signData = mac.doFinal(new byte[]{});
                String sign = new String(Base64Utils.encode(signData));
                jsonObject.put("timestamp", timestamp);
                jsonObject.put("sign", sign);
            }
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
     * @author ZhouYiXun
     * @des 发送每次测试结果到飞书群
     * @date 2021/8/20 18:29
     */
    @Override
    public void sendResultFinishReport(RestTemplate restTemplate, String token, String secret, String suiteName, int pass, int warn, int fail, int projectId, int resultId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg_type", "interactive");
        JSONObject card = new JSONObject();
        JSONObject config = new JSONObject();
        config.put("wide_screen_mode", true);
        card.put("config", config);
        JSONObject element = new JSONObject();
        element.put("tag", "markdown");
        List<JSONObject> elementList = new ArrayList<>();
        element.put("content", "**测试套件: " + suiteName + " 运行完毕！**\n" +
                "通过数：" + pass + " \n" +
                "异常数：" + warn + " \n" +
                "失败数：" + fail + "\n" +
                "测试报告：[点击查看](" + clientHost + "/Home/" + projectId + "/ResultDetail/" + resultId + ")");
        elementList.add(element);
        card.put("elements", elementList);
        jsonObject.put("card", card);
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
        jsonObject.put("msg_type", "interactive");
        JSONObject card = new JSONObject();
        JSONObject config = new JSONObject();
        config.put("wide_screen_mode", true);
        card.put("config", config);
        JSONObject element = new JSONObject();
        element.put("tag", "markdown");
        List<JSONObject> elementList = new ArrayList<>();
        element.put("content", "**Sonic云真机测试平台日报**\n" +
                "项目：" + projectName + " \n" +
                "时间：" + yesterday + " ～ " + today + " \n" +
                "通过数：" + passCount + "\n" +
                "异常数：" + warnCount + " \n" +
                "失败数：" + failCount + " \n" +
                "测试通过率：" + (total > 0 ?
                new BigDecimal(((float) passCount / total) * 100).setScale(2, RoundingMode.HALF_UP).doubleValue() : 0) + "% \n" +
                "详细统计：[点击查看](" + clientHost + "/Home/" + projectId + ")");
        elementList.add(element);
        card.put("elements", elementList);
        jsonObject.put("card", card);
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
        jsonObject.put("msg_type", "interactive");
        JSONObject card = new JSONObject();
        JSONObject config = new JSONObject();
        config.put("wide_screen_mode", true);
        card.put("config", config);
        JSONObject element = new JSONObject();
        element.put("tag", "markdown");
        List<JSONObject> elementList = new ArrayList<>();

        if (errorType == 1) {
            element.put("content", "**Sonic设备高温预警** \n" +
                    "设备序列号：" + udId + " \n" +
                    "电池温度：" + (tem / 10) + " ℃");
        } else {
            element.put("content", "**Sonic设备高温超时，已关机！** \n" +
                    "设备序列号：" + udId + " \n" +
                    "电池温度：" + (tem / 10) + " ℃");
        }
        elementList.add(element);
        card.put("elements", elementList);
        jsonObject.put("card", card);
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
        jsonObject.put("msg_type", "interactive");
        JSONObject card = new JSONObject();
        JSONObject config = new JSONObject();
        config.put("wide_screen_mode", true);
        card.put("config", config);
        JSONObject element = new JSONObject();
        element.put("tag", "markdown");
        List<JSONObject> elementList = new ArrayList<>();
        element.put("content", "**Sonic云真机测试平台周报**\n" +
                "项目：" + projectName + " \n" +
                "时间：" + yesterday + " ～ " + today + " \n" +
                "共测试：" + count + " 次\n" +
                "通过数：" + passCount + " \n" +
                "异常数：" + warnCount + " \n" +
                "失败数：" + failCount + " \n" +
                "测试通过率：" + (total > 0 ?
                new BigDecimal(((float) passCount / total) * 100).setScale(2, RoundingMode.HALF_UP).doubleValue() : 0) + "% \n" +
                "详细统计：[点击查看](" + clientHost + "/Home/" + projectId + ")");
        elementList.add(element);
        card.put("elements", elementList);
        jsonObject.put("card", card);
        this.signAndSend(restTemplate, token, secret, jsonObject);
    }
}
