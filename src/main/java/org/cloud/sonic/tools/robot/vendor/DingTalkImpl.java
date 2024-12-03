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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.Expression;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * @author ayumi760405
 * @des 钉钉机器人推送实作类，可以参考 https://developers.dingtalk.com/document/app/push-robots
 * @date 2022/12/20
 */
@Slf4j
@Service("DingTalkImpl")
public class DingTalkImpl implements RobotMessenger {

    @Configuration
    static class DingTalkMsgExt {
        @Value("${robot.img.success}")
        public String successUrl;
        //警告时的图片url
        @Value("${robot.img.warning}")
        public String warningUrl;
        //失败时的图片url
        @Value("${robot.img.error}")
        public String errorUrl;
    }
    @Autowired
    private DingTalkMsgExt ext;

    Expression templateTestSuiteMessage = RobotMessenger.parseTemplate("""
            #{
            {
              msgtype: 'link',
              link: {
                title: '测试套件:' + suiteName + '运行完毕！',
                text: '通过数：'+pass+'
            异常数：'+warn+'
            失败数：'+fail,
                messageUrl: url,
                picUrl: (fail > 0 ? ext.errorUrl : (warn > 0 ? ext.warningUrl : ext.successUrl))
              }
            }
            }""");
    Expression templateProjectSummaryMessage = RobotMessenger.parseTemplate("""
            #{
            {
              msgtype: 'markdown',
              markdown: {
               title: 'Sonic云真机测试平台'+(isWeekly?'周':'日')+'报',
               text: '### Sonic云真机测试平台'+(isWeekly ? '周': '日')+'报
            > ###### 项目：'+projectName+'
            > ###### 时间：'+getFormat().format(startDate)+' ～ '+getFormat().format(endDate)+'
            > ###### 共测试：'+total+'次
            > ###### 通过数：<font color=#67C23A>'+pass+'</font>
            > ###### 异常数：<font color='+(warn==0?'#67C23A':'#F56C6C')+'>'+warn+'</font>
            > ###### 失败数：<font color='+(warn==0?'#67C23A':'#F56C6C')+'>'+fail+'</font>
            > ###### 测试通过率：'+rate+'%
            > ###### 详细统计：[点击查看]('+url+')'
              }
            }
            }""");
    Expression templateDeviceMessage = RobotMessenger.parseTemplate("""
            #{
            { msgtype: 'markdown',
              markdown: {
                title: '设备温度异常通知',
                text: '### Sonic设备高温'+(errorType == 1 ? '预警' : '超时，已关机！')+'
            > ###### 设备序列号：'+udId+'
            > ###### 电池温度：<font color=#F56C6C>'+tem+' ℃</font>'
              }
            }
            }""");

    /**
     * @param restTemplate RestTemplate
     * @param token        机器人token
     * @param secret       机器人密钥
     * @param jsonObject   通知内容
     * @author ZhouYiXun
     * @des 钉钉官方签名方法
     * @date 2021/8/20 18:20
     */
    private void signAndSend(RestTemplate restTemplate, String token, String secret, Map<?, ?> jsonObject) {
        try {
            String path = "";
            if (!StringUtils.isEmpty(secret)) {
                Long timestamp = System.currentTimeMillis();
                String stringToSign = timestamp + "\n" + secret;
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
                byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
                String sign = URLEncoder.encode(new String(Base64.getEncoder().encode(signData)), StandardCharsets.UTF_8);
                path = "&timestamp=" + timestamp + "&sign=" + sign;
            }

            ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(token + path, jsonObject, JSONObject.class);
            log.info("robot result: " + responseEntity.getBody());
        } catch (Exception e) {
            log.warn("robot send failed, cause: " + e.getMessage());
        }
    }

    @Override
    public void sendMessage(RestTemplate restTemplate, String token, String secret, Expression messageTemplate, Message msg) {
        msg.ext = ext;
        Map<?, ?> content = messageTemplate.getValue(ctx, msg, Map.class);
        this.signAndSend(restTemplate, token, secret, content);
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
