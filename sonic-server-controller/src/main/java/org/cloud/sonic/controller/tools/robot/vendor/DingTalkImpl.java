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

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.controller.tools.robot.RobotMessenger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;

/**
 * @author ayumi760405
 * @des 钉钉机器人推送实作类，可以参考 https://developers.dingtalk.com/document/app/push-robots
 * @date 2022/12/20
 */
@Slf4j
@Service("DingTalkImpl")
public class DingTalkImpl implements RobotMessenger {

    //从配置文件获取前端部署的host
    @Value("${robot.client.host}")
    private String clientHost;
    //成功时的图片url
    @Value("${robot.img.success}")
    private String successUrl;
    //警告时的图片url
    @Value("${robot.img.warning}")
    private String warningUrl;
    //失败时的图片url
    @Value("${robot.img.error}")
    private String errorUrl;

    /**
     * @param restTemplate RestTemplate
     * @param token        机器人token
     * @param secret       机器人密钥
     * @param jsonObject   通知内容
     * @author ZhouYiXun
     * @des 钉钉官方签名方法
     * @date 2021/8/20 18:20
     */
    private void signAndSend(RestTemplate restTemplate, String token, String secret, JSONObject jsonObject) {
        clientHost = clientHost.replace(":80/", "/");
        try {
            String path = "";
            if (!StringUtils.isEmpty(secret)) {
                Long timestamp = System.currentTimeMillis();
                String stringToSign = timestamp + "\n" + secret;
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
                byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
                String sign = URLEncoder.encode(new String(Base64Utils.encode(signData)), "UTF-8");
                path = "&timestamp=" + timestamp + "&sign=" + sign;
            }

            ResponseEntity<JSONObject> responseEntity =
                    restTemplate.postForEntity(token + path
                            , jsonObject, JSONObject.class);
            log.info("robot result: " + responseEntity.getBody());
        } catch (Exception e) {
            log.warn("robot send failed, cause: " + e.getMessage());
        }
    }

    /**
     * @param restTemplate RestTemplate
     * @param token        机器人token
     * @param secret       机器人密钥
     * @param suiteName    套件名称
     * @param pass         通过数量
     * @param warn         警告数量
     * @param fail         失败数量
     * @param projectId    项目id
     * @param resultId     结果id
     * @return void
     * @author ZhouYiXun
     * @des 发送每次测试结果到钉钉
     * @date 2021/8/20 18:29
     */
    @Override
    public void sendResultFinishReport(RestTemplate restTemplate, String token, String secret, String suiteName, int pass, int warn, int fail, int projectId, int resultId) {
        JSONObject jsonObject = new JSONObject();
        JSONObject link = new JSONObject();
        link.put("text", "通过数：" + pass +
                " \n异常数：" + warn +
                " \n失败数：" + fail);
        link.put("title", "测试套件: " + suiteName + " 运行完毕！");
        link.put("messageUrl", clientHost + "/Home/" + projectId + "/ResultDetail/" + resultId);
        //判断测试结果，来决定显示什么图片
        if (fail > 0) {
            link.put("picUrl", errorUrl);
        } else if (warn > 0) {
            link.put("picUrl", warningUrl);
        } else {
            link.put("picUrl", successUrl);
        }
        jsonObject.put("msgtype", "link");
        jsonObject.put("link", link);
        this.signAndSend(restTemplate, token, secret, jsonObject);
    }

    /**
     * @param restTemplate RestTemplate
     * @param token        机器人token
     * @param secret       机器人密钥
     * @param projectId    项目id
     * @param projectName  项目名称
     * @param yesterday    昨天的起始时间
     * @param today        今天的起始时间
     * @return void
     * @author ZhouYiXun
     * @des 发送日报
     * @date 2021/8/20 18:42
     */
    @Override
    public void sendDayReportMessage(RestTemplate restTemplate, String token, String secret, int projectId, String projectName, String yesterday, String today, int passCount, int warnCount, int failCount) {
        JSONObject jsonObject = new JSONObject();
        int total = passCount + warnCount + failCount;
        JSONObject markdown = new JSONObject();
        //根据三个数量来决定markdown的字体颜色
        String failColorString;
        if (failCount == 0) {
            failColorString = "<font color=#67C23A>" + failCount + "</font>";
        } else {
            failColorString = "<font color=#F56C6C>" + failCount + "</font>";
        }
        String warnColorString;
        if (warnCount == 0) {
            warnColorString = "<font color=#67C23A>" + warnCount + "</font>";
        } else {
            warnColorString = "<font color=#E6A23C>" + warnCount + "</font>";
        }
        markdown.put("text", "### Sonic云真机测试平台日报 \n" +
                "> ###### 项目：" + projectName + " \n" +
                "> ###### 时间：" + yesterday + " ～ " + today + " \n" +
                "> ###### 通过数：<font color=#67C23A>" + passCount + "</font> \n" +
                "> ###### 异常数：" + warnColorString + " \n" +
                "> ###### 失败数：" + failColorString + " \n" +
                "> ###### 测试通过率：" + (total > 0 ?
                new BigDecimal(((float) passCount / total) * 100).setScale(2, RoundingMode.HALF_UP).doubleValue() : 0) + "% \n" +
                "> ###### 详细统计：[点击查看](" + clientHost + "/Home/" + projectId + ")");
        markdown.put("title", "Sonic云真机测试平台日报");
        jsonObject.put("msgtype", "markdown");
        jsonObject.put("markdown", markdown);
        this.signAndSend(restTemplate, token, secret, jsonObject);
    }

    /**
     * @param restTemplate RestTemplate
     * @param token        机器人token
     * @param secret       机器人密钥
     * @param errorType    errorType
     * @param tem          温度
     * @param udId         设备Id
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
            markdown.put("text", "### Sonic设备高温预警 \n" +
                    "> ###### 设备序列号：" + udId + " \n" +
                    "> ###### 电池温度：<font color=#F56C6C>" + (tem / 10) + " ℃</font>");
        } else {
            markdown.put("text", "### Sonic设备高温超时，已关机！ \n" +
                    "> ###### 设备序列号：" + udId + " \n" +
                    "> ###### 电池温度：<font color=#F56C6C>" + (tem / 10) + " ℃</font>");
        }
        markdown.put("title", "设备温度异常通知");
        jsonObject.put("msgtype", "markdown");
        jsonObject.put("markdown", markdown);
        this.signAndSend(restTemplate, token, secret, jsonObject);
    }

    /**
     * @param restTemplate RestTemplate
     * @param token        机器人token
     * @param secret       机器人密钥
     * @param projectId    项目id
     * @param projectName  项目名称
     * @param yesterday    昨天的起始时间
     * @param today        今天的起始时间
     * @param passCount    通过数量
     * @param warnCount    警告数量
     * @param failCount    失败数量
     * @param count        测试数量
     * @return void
     * @author ZhouYiXun
     * @des 发送周报
     * @date 2021/8/20 18:42
     */
    @Override
    public void sendWeekReportMessage(RestTemplate restTemplate, String token, String secret, int projectId, String projectName, String yesterday, String today, int passCount, int warnCount, int failCount, int count) {
        JSONObject jsonObject = new JSONObject();
        int total = passCount + warnCount + failCount;
        JSONObject markdown = new JSONObject();
        //根据三个数量来决定markdown的字体颜色
        String failColorString;
        if (failCount == 0) {
            failColorString = "<font color=#67C23A>" + failCount + "</font>";
        } else {
            failColorString = "<font color=#F56C6C>" + failCount + "</font>";
        }
        String warnColorString;
        if (warnCount == 0) {
            warnColorString = "<font color=#67C23A>" + warnCount + "</font>";
        } else {
            warnColorString = "<font color=#E6A23C>" + warnCount + "</font>";
        }
        markdown.put("text", "### Sonic云真机测试平台周报 \n" +
                "> ###### 项目：" + projectName + " \n" +
                "> ###### 时间：" + yesterday + " ～ " + today + " \n" +
                "> ###### 共测试：" + count + " 次\n" +
                "> ###### 通过数：<font color=#67C23A>" + passCount + "</font> \n" +
                "> ###### 异常数：" + warnColorString + " \n" +
                "> ###### 失败数：" + failColorString + " \n" +
                "> ###### 测试通过率：" + (total > 0 ?
                new BigDecimal(((float) passCount / total) * 100).setScale(2, RoundingMode.HALF_UP).doubleValue() : 0) + "% \n" +
                "> ###### 详细统计：[点击查看](" + clientHost + "/Home/" + projectId + ")");
        markdown.put("title", "Sonic云真机测试平台周报");
        jsonObject.put("msgtype", "markdown");
        jsonObject.put("markdown", markdown);
        this.signAndSend(restTemplate, token, secret, jsonObject);
    }
}
