package com.sonic.controller.tools;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;

/**
 * @author ZhouYiXun
 * @des 钉钉推送相关工具类，可以参考 https://developers.dingtalk.com/document/app/push-robots
 * @date 2021/8/15 18:20
 */
@Component
@RefreshScope
public class DingTalkMsgTool {
    private final Logger logger = LoggerFactory.getLogger(DingTalkMsgTool.class);
    //从配置文件获取前端部署的host
    @Value("${client.host}")
    private String clientHost;

    @Autowired
    private RestTemplate restTemplate;
    //成功时的图片url，请填写可以公网访问的链接
    private String successUrl = "";
    //警告时的图片url，请填写可以公网访问的链接
    private String warningUrl = "";
    //失败时的图片url，请填写可以公网访问的链接
    private String errorUrl = "";

    /**
     * @param token      机器人token
     * @param secret     机器人密钥
     * @param jsonObject 通知内容
     * @author ZhouYiXun
     * @des 钉钉官方签名方法
     * @date 2021/8/20 18:20
     */
    private void signAndSend(String token, String secret, JSONObject jsonObject) {
        try {
            Long timestamp = System.currentTimeMillis();
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            String sign = URLEncoder.encode(new String(Base64Utils.encode(signData)), "UTF-8");
            ResponseEntity<JSONObject> responseEntity =
                    restTemplate.postForEntity(token + "&timestamp=" + timestamp + "&sign=" + sign
                            , jsonObject, JSONObject.class);
            logger.info("钉钉发送结果：" + responseEntity.getBody());
        } catch (Exception e) {
            logger.info("钉钉发送失败：" + e.getMessage());
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
                                       int warn, int fail, int projectId, int resultId) {
        JSONObject jsonObject = new JSONObject();
        JSONObject link = new JSONObject();
        link.put("text", "通过数：" + pass +
                " \n异常数：" + warn +
                " \n失败数：" + fail);
        link.put("title", "测试套件: " + suiteName + " 运行完毕！");
        link.put("messageUrl", "http://" + clientHost + "/Sonic/Home/" + projectId + "/ResultInfo/" + resultId);
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
        signAndSend(token, secret, jsonObject);
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
    public void sendInstallPackageFinishReport(String token, String secret, String platform,
                                               String version, String url, JSONArray detail) {
        JSONObject jsonObject = new JSONObject();
        JSONObject markdown = new JSONObject();
        String device = "";
        //遍历详情里面的结果，组装成多条安装结果的markdown
        for (Object o : detail) {
            JSONObject deviceDetail = (JSONObject) o;
            String statusColor;
            if (deviceDetail.getString("status").equals("PASS")) {
                statusColor = "<font color=#67C23A>PASS</font>";
            } else {
                statusColor = "<font color=#F56C6C>FAIL</font>";
            }
            device += ("> ###### " + deviceDetail.getString("name") + "  ---  " + statusColor + " \n");
        }
        markdown.put("text", "#### **Sonic装包完成通知** \n" +
                "  ###### 平台：" + platform + " \n" +
                "  ###### 版本号：" + (version.length() == 0 ? "未知版本" : version) + " \n" +
                device +
                "  ###### 安装地址：[点击查看](" + url + ")");
        markdown.put("title", "Sonic装包完成通知");
        jsonObject.put("msgtype", "markdown");
        jsonObject.put("markdown", markdown);
        signAndSend(token, secret, jsonObject);
    }

    /**
     * @param token       机器人token
     * @param secret      机器人密钥
     * @param projectId   项目id
     * @param projectName 项目名称
     * @param yesterday   昨天的起始时间
     * @param today       今天的起始时间
     * @param time        运行时长
     * @param crashNum    崩溃数量
     * @param lagNum      卡顿数量
     * @param noFixNum    未修复数量
     * @return void
     * @author ZhouYiXun
     * @des 发送日报
     * @date 2021/8/20 18:42
     */
    public void sendDayReportMessage(String token, String secret, int projectId, String projectName,
                                     String yesterday, String today, String time,
                                     int crashNum, int lagNum, int noFixNum) {
        JSONObject jsonObject = new JSONObject();
        JSONObject markdown = new JSONObject();
        //根据三个数量来决定markdown的字体颜色
        String crashColorString;
        if (crashNum == 0) {
            crashColorString = "<font color=#67C23A>" + crashNum + "</font>";
        } else {
            crashColorString = "<font color=#F56C6C>" + crashNum + "</font>";
        }
        String lagColorString;
        if (lagNum == 0) {
            lagColorString = "<font color=#67C23A>" + lagNum + "</font>";
        } else {
            lagColorString = "<font color=#F56C6C>" + lagNum + "</font>";
        }
        String notFixColorString;
        if (noFixNum == 0) {
            notFixColorString = "<font color=#67C23A>" + noFixNum + "</font>";
        } else {
            notFixColorString = "<font color=#F56C6C>" + noFixNum + "</font>";
        }
        markdown.put("text", "### Sonic测试平台日报 \n" +
                "> ###### 项目：" + projectName + " \n" +
                "> ###### 时间：" + yesterday + " ～ " + today + " \n" +
                "> ###### 运行时长：" + time + " \n" +
                "> ###### 发现崩溃：" + crashColorString + "   卡顿：" + lagColorString + "\n" +
                "> ###### 未修复问题：" + notFixColorString + "个 \n" +
                "> ###### 问题列表：[点击查看](http://" + clientHost + "/Sonic/Home/" + projectId + "/Crash)");
        markdown.put("title", "Sonic测试平台日报");
        jsonObject.put("msgtype", "markdown");
        jsonObject.put("markdown", markdown);
        signAndSend(token, secret, jsonObject);
    }
}
