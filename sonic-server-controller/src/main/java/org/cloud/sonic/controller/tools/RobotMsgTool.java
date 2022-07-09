/*
 *  Copyright (C) [SonicCloudOrg] Sonic Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.cloud.sonic.controller.tools;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.controller.models.interfaces.RobotType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhouYiXun
 * @des 机器人推送相关工具类，可以参考 https://developers.dingtalk.com/document/app/push-robots
 * @date 2021/8/15 18:20
 */
@Component
public class RobotMsgTool {
    private final Logger logger = LoggerFactory.getLogger(RobotMsgTool.class);
    @Autowired
    private RestTemplate restTemplate;
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
     * @param token      机器人token
     * @param secret     机器人密钥
     * @param jsonObject 通知内容
     * @author ZhouYiXun
     * @des 钉钉官方签名方法
     * @date 2021/8/20 18:20
     */
    private void signAndSend(String token, String secret, int type, JSONObject jsonObject) {
        clientHost = clientHost.replace(":80/", "/");
        try {
            switch (type) {
                case RobotType.DingTalk: {
                    Long timestamp = System.currentTimeMillis();
                    String stringToSign = timestamp + "\n" + secret;
                    Mac mac = Mac.getInstance("HmacSHA256");
                    mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
                    byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
                    String sign = URLEncoder.encode(new String(Base64Utils.encode(signData)), "UTF-8");
                    ResponseEntity<JSONObject> responseEntity =
                            restTemplate.postForEntity(token + "&timestamp=" + timestamp + "&sign=" + sign
                                    , jsonObject, JSONObject.class);
                    logger.info("机器人发送结果：" + responseEntity.getBody());
                    break;
                }
                case RobotType.WeChat: {
                    ResponseEntity<JSONObject> responseEntity =
                            restTemplate.postForEntity(token , jsonObject, JSONObject.class);
                    logger.info("机器人发送结果：" + responseEntity.getBody());
                }

                    break;
                case RobotType.FeiShu: {
                    String timestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);
                    String stringToSign = timestamp + "\n" + secret;
                    Mac mac = Mac.getInstance("HmacSHA256");
                    mac.init(new SecretKeySpec(stringToSign.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
                    byte[] signData = mac.doFinal(new byte[]{});
                    String sign = new String(Base64Utils.encode(signData));
                    jsonObject.put("timestamp", timestamp);
                    jsonObject.put("sign", sign);
                    ResponseEntity<JSONObject> responseEntity =
                            restTemplate.postForEntity(token, jsonObject, JSONObject.class);
                    logger.info("robot result: " + responseEntity.getBody());
                    break;
                }
                case RobotType.YouSpace:
                    break;
            }
        } catch (Exception e) {
            logger.info("robot send failed, cause: " + e.getMessage());
        }
    }

    /**
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
     * @des 发送每次测试结果到钉钉
     * @date 2021/8/20 18:29
     */
    public void sendResultFinishReport(String token, String secret, String suiteName, int pass,
                                       int warn, int fail, int projectId, int resultId, int type) {
        JSONObject jsonObject = new JSONObject();
        if (type == RobotType.DingTalk) {
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
        }
        if (type == RobotType.WeChat) {
            jsonObject.put("msgtype", "markdown");
            JSONObject markdown = new JSONObject();
            markdown.put("content",  "**测试套件: " + suiteName + " 运行完毕！**\n" +
                    "通过数：" + pass + " \n" +
                    "异常数：" + warn + " \n" +
                    "失败数：" + fail + "\n" +
                    "测试报告：[点击查看](" + clientHost + "/Home/" + projectId + "/ResultDetail/" + resultId + ")");
            jsonObject.put("markdown", markdown);

        }
        if (type == RobotType.FeiShu) {
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
        }
        signAndSend(token, secret, type, jsonObject);
    }

    /**
     * @param token    机器人token
     * @param secret   机器人密钥
     * @param platform 平台
     * @param version  版本号
     * @param url      安装包链接
     * @param detail   具体安装情况
     * @return void
     * @author ZhouYiXun
     * @des 发送装包完毕通知
     * @date 2021/8/20 18:33
     */
//    public void sendInstallPackageFinishReport(String token, String secret, String platform,
//                                               String version, String url, JSONArray detail) {
//        JSONObject jsonObject = new JSONObject();
//        JSONObject markdown = new JSONObject();
//        String device = "";
//        //遍历详情里面的结果，组装成多条安装结果的markdown
//        for (Object o : detail) {
//            JSONObject deviceDetail = (JSONObject) o;
//            String statusColor;
//            if (deviceDetail.getString("status").equals("PASS")) {
//                statusColor = "<font color=#67C23A>PASS</font>";
//            } else {
//                statusColor = "<font color=#F56C6C>FAIL</font>";
//            }
//            device += ("> ###### " + deviceDetail.getString("name") + "  ---  " + statusColor + " \n");
//        }
//        markdown.put("text", "#### **Sonic装包完成通知** \n" +
//                "  ###### 平台：" + platform + " \n" +
//                "  ###### 版本号：" + (version.length() == 0 ? "未知版本" : version) + " \n" +
//                device +
//                "  ###### 安装地址：[点击查看](" + url + ")");
//        markdown.put("title", "Sonic装包完成通知");
//        jsonObject.put("msgtype", "markdown");
//        jsonObject.put("markdown", markdown);
//        signAndSend(token, secret, jsonObject);
//    }

    /**
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
    public void sendDayReportMessage(String token, String secret, int projectId, String projectName,
                                     String yesterday, String today, int passCount, int warnCount, int failCount, int type) {
        JSONObject jsonObject = new JSONObject();
        if (type == RobotType.DingTalk) {
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
            int total = passCount + warnCount + failCount;
            markdown.put("text", "### Sonic云真机测试平台日报 \n" +
                    "> ###### 项目：" + projectName + " \n" +
                    "> ###### 时间：" + yesterday + " ～ " + today + " \n" +
                    "> ###### 通过数：<font color=#67C23A>" + passCount + "</font> \n" +
                    "> ###### 异常数：" + warnColorString + " \n" +
                    "> ###### 失败数：" + failColorString + " \n" +
                    "> ###### 测试通过率：" + (total > 0 ?
                    new BigDecimal((float) passCount / total).setScale(2, RoundingMode.HALF_UP).doubleValue() : 0) + "% \n" +
                    "> ###### 详细统计：[点击查看](" + clientHost + "/Home/" + projectId + ")");
            markdown.put("title", "Sonic云真机测试平台日报");
            jsonObject.put("msgtype", "markdown");
            jsonObject.put("markdown", markdown);
        }
        if (type == RobotType.WeChat) {
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
            int total = passCount + warnCount + failCount;
            jsonObject.put("msgtype", "markdown");
            JSONObject markdown = new JSONObject();
            markdown.put("content", "### Sonic云真机测试平台日报 \n" +
                            "> ###### 项目：" + projectName + " \n" +
                            "> ###### 时间：" + yesterday + " ～ " + today + " \n" +
                            "> ###### 通过数：<font color=\"info\">" + passCount + "</font> \n" +
                            "> ###### 异常数：" + warnColorString + " \n" +
                            "> ###### 失败数：" + failColorString + " \n" +
                            "> ###### 测试通过率：" + (total > 0 ?
                            new BigDecimal((float) passCount / total).setScale(2, RoundingMode.HALF_UP).doubleValue() : 0) + "% \n" +
                            "> ###### 详细统计：[点击查看](" + clientHost + "/Home/" + projectId + ")");
            jsonObject.put("markdown", markdown);
        }
        if (type == RobotType.FeiShu) {
            jsonObject.put("msg_type", "interactive");
            JSONObject card = new JSONObject();
            JSONObject config = new JSONObject();
            config.put("wide_screen_mode", true);
            card.put("config", config);
            JSONObject element = new JSONObject();
            element.put("tag", "markdown");
            int total = passCount + warnCount + failCount;
            List<JSONObject> elementList = new ArrayList<>();
            element.put("content", "**Sonic云真机测试平台日报**\n" +
                    "项目：" + projectName + " \n" +
                    "时间：" + yesterday + " ～ " + today + " \n" +
                    "通过数：" + passCount + "\n" +
                    "异常数：" + warnCount + " \n" +
                    "失败数：" + failCount + " \n" +
                    "测试通过率：" + (total > 0 ?
                    new BigDecimal((float) passCount / total).setScale(2, RoundingMode.HALF_UP).doubleValue() : 0) + "% \n" +
                    "详细统计：[点击查看](" + clientHost + "/Home/" + projectId + ")");
            elementList.add(element);
            card.put("elements", elementList);
            jsonObject.put("card", card);
        }
        signAndSend(token, secret, type, jsonObject);
    }

    public void sendErrorDevice(String token, String secret, int type, int errorType, int tem, String udId) {
        JSONObject jsonObject = new JSONObject();
        if (type == RobotType.DingTalk) {
            JSONObject markdown = new JSONObject();
            if (errorType == 1) {
                markdown.put("text", "### 设备高温预警 \n" +
                        "> ###### 设备序列号：" + udId + " \n" +
                        "> ###### 电池温度：<font color=#F56C6C>" + (tem / 10) + " ℃</font>");
            } else {
                markdown.put("text", "### 设备高温超时，已关机！ \n" +
                        "> ###### 设备序列号：" + udId + " \n" +
                        "> ###### 电池温度：<font color=#F56C6C>" + (tem / 10) + " ℃</font>");
            }
            markdown.put("title", "设备温度异常通知");
            jsonObject.put("msgtype", "markdown");
            jsonObject.put("markdown", markdown);
        }

        if (type == RobotType.WeChat) {
            JSONObject markdown = new JSONObject();
            if (errorType == 1) {
                markdown.put("content", "### 设备高温预警 \n" +
                        "> ###### 设备序列号：" + udId + " \n" +
                        "> ###### 电池温度：<font color=\"warning\">" + (tem / 10) + " ℃</font>");
            } else {
                markdown.put("text", "### 设备高温超时，已关机！ \n" +
                        "> ###### 设备序列号：" + udId + " \n" +
                        "> ###### 电池温度：<font color=\"warning\">" + (tem / 10) + " ℃</font>");
            }
            jsonObject.put("msgtype", "markdown");
            jsonObject.put("markdown", markdown);
        }

        if (type == RobotType.FeiShu) {
            jsonObject.put("msg_type", "interactive");
            JSONObject card = new JSONObject();
            JSONObject config = new JSONObject();
            config.put("wide_screen_mode", true);
            card.put("config", config);
            JSONObject element = new JSONObject();
            element.put("tag", "markdown");
            List<JSONObject> elementList = new ArrayList<>();

            if (errorType == 1) {
                element.put("content", "**设备高温预警** \n" +
                        "设备序列号：" + udId + " \n" +
                        "电池温度：" + (tem / 10) + " ℃");
            } else {
                element.put("text", "### 设备高温超时，已关机！ \n" +
                        "设备序列号：" + udId + " \n" +
                        "电池温度：" + (tem / 10) + " ℃");
            }
            elementList.add(element);
            card.put("elements", elementList);
            jsonObject.put("card", card);
        }

        signAndSend(token, secret, type, jsonObject);
    }

    public void sendWeekReportMessage(String token, String secret, int projectId, String projectName,
                                      String yesterday, String today, int passCount, int warnCount, int failCount, int count, int type) {
        JSONObject jsonObject = new JSONObject();
        if (type == RobotType.DingTalk) {
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
            int total = passCount + warnCount + failCount;
            markdown.put("text", "### Sonic云真机测试平台周报 \n" +
                    "> ###### 项目：" + projectName + " \n" +
                    "> ###### 时间：" + yesterday + " ～ " + today + " \n" +
                    "> ###### 共测试：" + count + " 次\n" +
                    "> ###### 通过数：<font color=#67C23A>" + passCount + "</font> \n" +
                    "> ###### 异常数：" + warnColorString + " \n" +
                    "> ###### 失败数：" + failColorString + " \n" +
                    "> ###### 测试通过率：" + (total > 0 ?
                    new BigDecimal((float) passCount / total).setScale(2, RoundingMode.HALF_UP).doubleValue() : 0) + "% \n" +
                    "> ###### 详细统计：[点击查看](" + clientHost + "/Home/" + projectId + ")");
            markdown.put("title", "Sonic云真机测试平台周报");
            jsonObject.put("msgtype", "markdown");
            jsonObject.put("markdown", markdown);
        }
        if (type == RobotType.WeChat) {
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
            int total = passCount + warnCount + failCount;
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
                    new BigDecimal((float) passCount / total).setScale(2, RoundingMode.HALF_UP).doubleValue() : 0) + "% \n" +
                    "> ###### 详细统计：[点击查看](" + clientHost + "/Home/" + projectId + ")");
            jsonObject.put("markdown", markdown);
        }

        if (type == RobotType.FeiShu) {
            jsonObject.put("msg_type", "interactive");
            JSONObject card = new JSONObject();
            JSONObject config = new JSONObject();
            config.put("wide_screen_mode", true);
            card.put("config", config);
            JSONObject element = new JSONObject();
            element.put("tag", "markdown");
            int total = passCount + warnCount + failCount;
            List<JSONObject> elementList = new ArrayList<>();
            element.put("content", "**Sonic云真机测试平台周报**\n" +
                    "项目：" + projectName + " \n" +
                    "时间：" + yesterday + " ～ " + today + " \n" +
                    "共测试：" + count + " 次\n" +
                    "通过数：" + passCount + "</font> \n" +
                    "异常数：" + warnCount + " \n" +
                    "失败数：" + failCount + " \n" +
                    "测试通过率：" + (total > 0 ?
                    new BigDecimal((float) passCount / total).setScale(2, RoundingMode.HALF_UP).doubleValue() : 0) + "% \n" +
                    "详细统计：[点击查看](" + clientHost + "/Home/" + projectId + ")");
            elementList.add(element);
            card.put("elements", elementList);
            jsonObject.put("card", card);
        }
        signAndSend(token, secret, type, jsonObject);
    }
}
