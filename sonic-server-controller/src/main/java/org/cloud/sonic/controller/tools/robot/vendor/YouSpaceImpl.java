package org.cloud.sonic.controller.tools.robot.vendor;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.controller.tools.robot.RobotMessenger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ayumi760405
 * @des 友空间机器人推送实作类
 * @date 2022/12/20
 */
@Slf4j
@Component("YouSpaceImpl")
public class YouSpaceImpl implements RobotMessenger {

    //从配置文件获取前端部署的host
    @Value("${robot.client.host}")
    private String clientHost;

    /**
     * @param restTemplate RestTemplate
     * @param token      机器人token
     * @param secret     机器人密钥
     * @param jsonObject 通知内容
     * @author ZhouYiXun
     * @des 友空间签名方法
     * @date 2021/8/20 18:20
     */
    private void signAndSend(RestTemplate restTemplate, String token, String secret, JSONObject jsonObject) {
        clientHost = clientHost.replace(":80/", "/");
        try {
            JSONObject you = new JSONObject();
            you.put("timestamp", System.currentTimeMillis());
            you.put("content", Base64Utils.encodeToString(jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8)));
            ResponseEntity<JSONObject> responseEntity =
                    restTemplate.postForEntity(token
                            , you, JSONObject.class);
            log.info("robot result: " + responseEntity.getBody());
        } catch (Exception e) {
            log.warn("robot send failed, cause: " + e.getMessage());
        }
    }

    /**
     * @param s         讯息内容
     * @return JSONObject
     * @author ZhouYiXun
     * @des 将字串转为友空间格式
     * @date 2021/8/20 18:29
     */
    private JSONObject generateYouTextView(String s) {
        JSONObject text = new JSONObject();
        text.put("type", "textView");
        JSONObject data = new JSONObject();
        data.put("text", s);
        data.put("level", 1);
        text.put("data", data);
        return text;
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
     * @des 发送每次测试结果到友空间
     * @date 2021/8/20 18:29
     */
    @Override
    public void sendResultFinishReport(RestTemplate restTemplate, String token, String secret, String suiteName, int pass, int warn, int fail, int projectId, int resultId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("businessId", "测试套件: " + suiteName + " 运行完毕！");
        JSONObject titleZone = new JSONObject();
        titleZone.put("type", 0);
        titleZone.put("text", "测试套件: " + suiteName + " 运行完毕！");
        jsonObject.put("titleZone", titleZone);
        List<JSONObject> contentZone = new ArrayList<>();
        contentZone.add(generateYouTextView("通过数：" + pass));
        contentZone.add(generateYouTextView("异常数：" + warn));
        contentZone.add(generateYouTextView("失败数：" + fail));
        JSONObject button = new JSONObject();
        button.put("type", "buttonView");
        JSONObject data = new JSONObject();
        data.put("mode", 0);
        data.put("text", "点击查看测试报告");
        data.put("url", clientHost + "/Home/" + projectId + "/ResultDetail/" + resultId);
        button.put("data", data);
        contentZone.add(button);
        jsonObject.put("contentZone", contentZone);
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
        jsonObject.put("businessId", "Sonic云真机测试平台日报");
        JSONObject titleZone = new JSONObject();
        titleZone.put("type", 0);
        titleZone.put("text", "Sonic云真机测试平台日报");
        jsonObject.put("titleZone", titleZone);
        List<JSONObject> contentZone = new ArrayList<>();
        contentZone.add(generateYouTextView("项目：" + projectName));
        contentZone.add(generateYouTextView("时间：" + yesterday + " ～ " + today));
        contentZone.add(generateYouTextView("通过数：" + passCount));
        contentZone.add(generateYouTextView("异常数：" + warnCount));
        contentZone.add(generateYouTextView("失败数：" + failCount));
        contentZone.add(generateYouTextView("测试通过率：" + (total > 0 ?
                new BigDecimal(((float) passCount / total) * 100).setScale(2, RoundingMode.HALF_UP).doubleValue() : 0) + "%"));
        JSONObject button = new JSONObject();
        button.put("type", "buttonView");
        JSONObject data = new JSONObject();
        data.put("mode", 0);
        data.put("text", "点击查看");
        data.put("url", clientHost + "/Home/" + projectId);
        button.put("data", data);
        contentZone.add(button);
        jsonObject.put("contentZone", contentZone);
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
        String t = "Sonic设备高温预警";
        if (errorType != 1) {
            t = "Sonic设备高温超时，已关机！";
        }
        jsonObject.put("businessId", t);
        JSONObject titleZone = new JSONObject();
        titleZone.put("type", 0);
        titleZone.put("text", t);
        jsonObject.put("titleZone", titleZone);
        List<JSONObject> contentZone = new ArrayList<>();
        contentZone.add(generateYouTextView("设备序列号：" + udId));
        contentZone.add(generateYouTextView("电池温度：" + (tem / 10) + " ℃"));
        jsonObject.put("contentZone", contentZone);
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
        jsonObject.put("businessId", "Sonic云真机测试平台周报");
        JSONObject titleZone = new JSONObject();
        titleZone.put("type", 0);
        titleZone.put("text", "Sonic云真机测试平台周报");
        jsonObject.put("titleZone", titleZone);
        List<JSONObject> contentZone = new ArrayList<>();
        contentZone.add(generateYouTextView("项目：" + projectName));
        contentZone.add(generateYouTextView("时间：" + yesterday + " ～ " + today));
        contentZone.add(generateYouTextView("共测试：" + count));
        contentZone.add(generateYouTextView("通过数：" + passCount));
        contentZone.add(generateYouTextView("异常数：" + warnCount));
        contentZone.add(generateYouTextView("失败数：" + failCount));
        contentZone.add(generateYouTextView("测试通过率：" + (total > 0 ?
                new BigDecimal(((float) passCount / total) * 100).setScale(2, RoundingMode.HALF_UP).doubleValue() : 0) + "%"));
        JSONObject button = new JSONObject();
        button.put("type", "buttonView");
        JSONObject data = new JSONObject();
        data.put("mode", 0);
        data.put("text", "点击查看");
        data.put("url", clientHost + "/Home/" + projectId);
        button.put("data", data);
        contentZone.add(button);
        jsonObject.put("contentZone", contentZone);
        this.signAndSend(restTemplate, token, secret, jsonObject);
    }
}
