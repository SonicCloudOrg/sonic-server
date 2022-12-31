package org.cloud.sonic.controller.tools.robot.vendor;

import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.controller.tools.robot.RobotMessenger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author ayumi760405
 * @des Line Notify 推送实作类，可以参考 https://notify-bot.line.me/doc/en/
 * @date 2022/12/29
 */
@Slf4j
@Service("LineNotifyImpl")
public class LineNotifyImpl implements RobotMessenger {

    //从配置文件获取前端部署的host
    @Value("${robot.client.host}")
    private String clientHost;
    //line notify的host
    private static final String LINE_NOTIFY_HOST = "https://notify-api.line.me/api/notify";

    /**
     * @param restTemplate RestTemplate
     * @param token      机器人token
     * @param secret     机器人密钥
     * @param String 通知内容
     * @author ayumi760405
     * @des Line Notify 传送讯息方法
     * @date 2022/12/29
     */
    private void signAndSend(RestTemplate restTemplate, String token, String message) {
        clientHost = clientHost.replace(":80/", "/");
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<String, String>();
            requestMap.add("message", message);
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(requestMap, headers);
            ResponseEntity<String> responseEntity =
                    restTemplate.postForEntity(LINE_NOTIFY_HOST, entity, String.class);
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
     * @param warnCount      警告数量
     * @param failCount      失败数量
     * @param projectId 项目id
     * @param resultId  结果id
     * @return void
     * @author ayumi760405
     * @des 发送每次测试结果到Line Notify
     * @date 2022/12/29
     */
    @Override
    public void sendResultFinishReport(RestTemplate restTemplate, String token, String secret, String suiteName, int pass, int warnCount, int failCount, int projectId, int resultId) {
        String failColorString;
        if (failCount == 0) {
            failColorString = String.valueOf(failCount);
        } else {
            failColorString = " `" + failCount + "`";
        }
        String warnColorString;
        if (warnCount == 0) {
            warnColorString = String.valueOf(warnCount);
        } else {
            warnColorString = " `" + warnCount + "`";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        builder.append("*Sonic云真机测试报告*").append("\n");
        builder.append("测试套件: ").append(suiteName).append(" 运行完毕！").append("\n");
        builder.append("通过数：").append(pass).append("\n");
        builder.append("异常数：").append(warnColorString).append("\n");
        builder.append("失败数：").append(failColorString).append("\n");
        builder.append("测试报告:").append(clientHost).append("/Home/").append(projectId).append("/ResultDetail/").append(resultId);
        this.signAndSend(restTemplate, token, builder.toString());
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
     * @date 2022/12/29
     */
    @Override
    public void sendDayReportMessage(RestTemplate restTemplate, String token, String secret, int projectId, String projectName, String yesterday, String today, int passCount, int warnCount, int failCount) {
        int total = passCount + warnCount + failCount;
        String failColorString;
        if (failCount == 0) {
            failColorString = String.valueOf(failCount);
        } else {
            failColorString = " `" + failCount + "`";
        }
        String warnColorString;
        if (warnCount == 0) {
            warnColorString = String.valueOf(warnCount);
        } else {
            warnColorString = " `" + warnCount + "`";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        builder.append("*Sonic云真机测试平台日报*").append("\n");
        builder.append("项目:").append(projectName).append("\n");
        builder.append("时间:").append(yesterday).append(" ～ ").append(today).append("\n");
        builder.append("通过数：").append(passCount).append("\n");
        builder.append("异常数：").append(warnColorString).append("\n");
        builder.append("失败数：").append(failColorString).append("\n");
        builder.append("测试通过率: ").append((total > 0 ?
                BigDecimal.valueOf(((float) passCount / total) * 100).setScale(2, RoundingMode.HALF_UP).doubleValue() : 0)).append("%").append("\n");
        builder.append("测试报告:").append(clientHost).append("/Home/").append(projectId);
        this.signAndSend(restTemplate, token, builder.toString());
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
     * @date 2022/12/29
     */
    @Override
    public void sendErrorDevice(RestTemplate restTemplate, String token, String secret, int errorType, int tem, String udId) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        builder.append("*设备温度异常通知*").append("\n");
        if (errorType == 1) {
            builder.append("Sonic设备高温预警").append("\n");
        }
        else {
            builder.append("Sonic设备高温超时，已关机").append("\n");
        }
        builder.append("设备序列号:").append(udId).append("\n");
        builder.append("电池温度:").append(tem / 10).append(" ℃").append("\n");
        this.signAndSend(restTemplate, token, builder.toString());
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
     * @date 2022/12/29
     */
    @Override
    public void sendWeekReportMessage(RestTemplate restTemplate, String token, String secret, int projectId, String projectName, String yesterday, String today, int passCount, int warnCount, int failCount, int count) {
        int total = passCount + warnCount + failCount;
        String failColorString;
        if (failCount == 0) {
            failColorString = String.valueOf(failCount);
        } else {
            failColorString = " `" + failCount + "`";
        }
        String warnColorString;
        if (warnCount == 0) {
            warnColorString = String.valueOf(warnCount);
        } else {
            warnColorString = " `" + warnCount + "`";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        builder.append("*Sonic云真机测试平台周报*").append("\n");
        builder.append("项目:").append(projectName).append("\n");
        builder.append("时间:").append(yesterday).append(" ～ ").append(today).append("\n");
        builder.append("共测试:").append(count).append("次").append("\n");
        builder.append("通过数：").append(passCount).append("\n");
        builder.append("异常数：").append(warnColorString).append("\n");
        builder.append("失败数：").append(failColorString).append("\n");
        builder.append("测试通过率: ").append((total > 0 ?
                BigDecimal.valueOf(((float) passCount / total) * 100).setScale(2, RoundingMode.HALF_UP).doubleValue() : 0)).append("%").append("\n");
        builder.append("测试报告:").append(clientHost).append("/Home/").append(projectId);
        this.signAndSend(restTemplate, token, builder.toString());
    }
}
