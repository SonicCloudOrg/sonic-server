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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.controller.tools.robot.RobotMessenger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author young(stephenwang1011)
 * @des Slack robot消息推送
 * @date 2023/2/10
 */
@Slf4j
@Service("SlackNotifyImpl")
public class SlackNotifyImpl implements RobotMessenger {

    //从配置文件获取前端部署的host
    @Value("${robot.client.host}")
    private String clientHost;

    /**
     * @param restTemplate RestTemplate
     * @param token        机器人token
     * @param secret       机器人密钥
     * @param jsonObject   通知内容
     * @author young(stephenwang1011)
     * @des SLACK发送测试通知
     * @date 2023/2/14
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
     * @param token        机器人token
     * @param secret       机器人密钥
     * @param suiteName    套件名称
     * @param passCount    通过数量
     * @param warnCount    警告数量
     * @param failCount    失败数量
     * @param projectId    项目id
     * @param resultId     结果id
     * @return void
     * @author young(stephenwang1011)
     * @des 发送每次测试结果到SLACK
     * @date 2023/2/14
     */
    @Override
    public void sendResultFinishReport(RestTemplate restTemplate, String token, String secret, String suiteName, int passCount, int warnCount, int failCount, int projectId, int resultId) {
        JSONObject slackObjs = new JSONObject(true);

        String reportLink = clientHost + "/Home/" + projectId + "/ResultDetail/" + resultId;
        SlackMessage slackMessage = new SlackMessage();
        slackMessage.setType("mrkdwn");
        slackMessage.setText("*" + suiteName + " 测试完成* \n" +
                "通过条数：" + passCount + "\n" +
                "异常条数：" + warnCount + "\n" +
                "失败条数：" + failCount + "\n" +
                "测试详情：" + reportLink);

        this.slackMessage(slackMessage, restTemplate, token, secret, slackObjs, suiteName);
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
     * @author young(stephenwang1011)
     * @des 通过slack发送day report
     * @date 2022/12/20
     */
    @Override
    public void sendDayReportMessage(RestTemplate restTemplate, String token, String secret, int projectId, String projectName, String yesterday, String today, int passCount, int warnCount, int failCount) {
        JSONObject slackObjs = new JSONObject(true);

        int total = passCount + warnCount + failCount;
        String reportLink = clientHost + "/Home/" + projectId;

        SlackMessage slackMessage = new SlackMessage();
        slackMessage.setType("mrkdwn");
        slackMessage.setText("*Sonic云真机测试平台日报* \n" +
                "项目名称：" + projectName + "\n" +
                "测试时间：" + yesterday + " - " + today + "\n" +
                "通过条数：" + passCount + "\n" +
                "异常条数：" + warnCount + "\n" +
                "失败条数：" + failCount + "\n" +
                "通  过  率：" + (total > 0 ?
                new BigDecimal(((float) passCount / total) * 100).setScale(2, RoundingMode.HALF_UP).doubleValue() : 0) + "%\n" +
                "测试详情：" + reportLink);

        this.slackMessage(slackMessage, restTemplate, token, secret, slackObjs, projectName);

    }


    /**
     * @param restTemplate RestTemplate
     * @param token        机器人token
     * @param secret       机器人密钥
     * @param errorType    errorType
     * @param tem          温度
     * @param udId         设备Id
     * @return void
     * @author young(stephenwang1011)
     * @des 向slack发送设备错误讯息
     * @date 2022/12/20
     */
    @Override
    public void sendErrorDevice(RestTemplate restTemplate, String token, String secret, int errorType, int tem, String udId) {

        JSONObject slackObjs = new JSONObject(true);
        SlackMessage slackMessage = new SlackMessage();
        slackMessage.setType("mrkdwn");

        if (errorType == 1) {
            slackMessage.setText("*Sonic设备高温预警* \n" +
                    "设备UDID：" + udId + "\n" +
                    "电池温度：" + (tem / 10) + "℃");

            //这个是slack消息的通知title
            slackObjs.put("text", "警告，设备：" + udId + "温度过高！");
        } else {
            slackMessage.setText("*Sonic设备高温超时，已关机* \n" +
                    "设备UDID：" + udId + "\n" +
                    "电池温度：" + (tem / 10) + "℃");

            //这个是slack消息的通知title
            slackObjs.put("text", "警告，设备：" + udId + "高温超时，已关机");
        }

        this.slackMessage(slackMessage, restTemplate, token, secret, slackObjs, udId);
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
     * @author young(stephenwang1011)
     * @des 发送周报到slack
     * @date 2022/12/20
     */
    @Override
    public void sendWeekReportMessage(RestTemplate restTemplate, String token, String secret, int projectId, String projectName, String yesterday, String today, int passCount, int warnCount, int failCount, int count) {
        JSONObject slackObjs = new JSONObject(true);
        int total = passCount + warnCount + failCount;
        String reportLink = clientHost + "/Home/" + projectId;

        SlackMessage slackMessage = new SlackMessage();
        slackMessage.setType("mrkdwn");
        slackMessage.setText("*Sonic云真机测试平台周报* \n" +
                "项目名称：" + projectName + "\n" +
                "测试时间：" + yesterday + " - " + today + "\n" +
                "一共测试：" + count + "次" + "\n" +
                "通过条数：" + passCount + "\n" +
                "异常条数：" + warnCount + "\n" +
                "失败条数：" + failCount + "\n" +
                "通  过  率：" + (total > 0 ?
                new BigDecimal(((float) passCount / total) * 100).setScale(2, RoundingMode.HALF_UP).doubleValue() : 0) + "%\n" +
                "测试详情：" + reportLink);

        this.slackMessage(slackMessage, restTemplate, token, secret, slackObjs, projectName);
    }

    public void slackMessage(SlackMessage slackMessage, RestTemplate restTemplate, String token, String secret, JSONObject slackObjs, String name) {

        Map<Object, Object> slackMap = new LinkedHashMap<>();
        slackMap.put("type", "section");
        slackMap.put("text", slackMessage);
        JSONArray blocksArray = new JSONArray();
        blocksArray.add(slackMap);

        //这个是slack消息的通知title
        slackObjs.put("text", name + "测试完成，请查看!");
        //这个是slack消息的具体内容
        slackObjs.put("blocks", blocksArray);

        this.signAndSend(restTemplate, token, secret, slackObjs);
    }


    static class SlackMessage {
        private String type;
        private String text;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

    }

}