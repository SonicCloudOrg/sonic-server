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
import org.cloud.sonic.controller.tools.robot.Message;
import org.cloud.sonic.controller.tools.robot.RobotMessenger;
import org.springframework.expression.Expression;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @author ayumi760405
 * @des 飞书群机器人推送实作类
 * @date 2022/12/20
 */
@Slf4j
@Service("FeiShuImpl")
public class FeiShuImpl implements RobotMessenger {

    Expression templateTestSuiteMessage = RobotMessenger.parseTemplate("""
            **测试套件: #{suiteName} 运行完毕！**
            通过数：#{pass}
            异常数：#{warn}
            失败数：#{fail}
            测试报告：[点击查看](#{url})""");
    Expression templateProjectSummaryMessage = RobotMessenger.parseTemplate("""
            **Sonic云真机测试平台#{isWeekly ? '周': '日'}报**
            项目：#{projectName}
            时间：#{getFormat().format(startDate)} ～ #{getFormat().format(endDate)}
            共测试：#{total}次
            通过数：#{pass}
            异常数：#{warn}
            失败数：#{fail}
            测试通过率：#{rate}%
            详细统计：[点击查看](#{url})""");
    Expression templateDeviceMessage = RobotMessenger.parseTemplate("""
            **Sonic设备高温#{errorType == 1 ? '预警' : '超时，已关机！'}**
            设备序列号：#{udId}
            电池温度：#{tem}℃""");

    /**
     * @param restTemplate RestTemplate
     * @param token        机器人token
     * @param jsonObject   通知内容
     */
    private void signAndSend(RestTemplate restTemplate, String token, String secret, JSONObject jsonObject) {
        try {
            if (!StringUtils.isEmpty(secret)) {
                String timestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);
                String stringToSign = timestamp + "\n" + secret;
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(new SecretKeySpec(stringToSign.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
                byte[] signData = mac.doFinal(new byte[]{});
                String sign = new String(Base64.getEncoder().encode(signData));
                jsonObject.put("timestamp", timestamp);
                jsonObject.put("sign", sign);
            }
            ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(token, jsonObject, JSONObject.class);
            log.info("robot result: " + responseEntity.getBody());
        } catch (Exception e) {
            log.warn("robot send failed, cause: " + e.getMessage());
        }
    }

    @Override
    public void sendMessage(RestTemplate restTemplate, String token, String secret, Expression messageTemplate, Message msg) {
        String content = messageTemplate.getValue(ctx, msg, String.class);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg_type", "interactive");
        JSONObject card = new JSONObject();
        JSONObject config = new JSONObject();
        config.put("wide_screen_mode", true);
        card.put("config", config);
        JSONObject element = new JSONObject();
        element.put("tag", "markdown");
        List<JSONObject> elementList = new ArrayList<>();
        element.put("content", content);
        elementList.add(element);
        card.put("elements", elementList);
        jsonObject.put("card", card);
        this.signAndSend(restTemplate, token, secret, jsonObject);
    }

    @Override
    public Expression getDefaultTestSuiteTemplate() {
        return templateTestSuiteMessage;
    }

    @Override
    public Expression getDefaultProjectSummaryTemplate() {
        return templateProjectSummaryMessage;
    }

    @Override
    public Expression getDefaultDeviceMessageTemplate() {
        return templateDeviceMessage;
    }

}
