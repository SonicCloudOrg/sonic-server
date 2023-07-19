package org.cloud.sonic.controller.tools.robot.vendor;

import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.controller.tools.robot.Message;
import org.cloud.sonic.controller.tools.robot.RobotMessenger;
import org.springframework.expression.Expression;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service("WebhookImpl")
public class WebhookImpl implements RobotMessenger {
    @Override
    public void sendMessage(RestTemplate restTemplate, String token, String secret, Expression messageTemplate, Message message) {
        if (messageTemplate == null) {
            ResponseEntity<Object> responseEntity = restTemplate.getForEntity(token, Object.class);
            log.info("robot result: " + responseEntity.getBody());
        } else {
            Object content = messageTemplate.getValue(ctx, message);
            ResponseEntity<Object> responseEntity = restTemplate.postForEntity(token, content, Object.class);
            log.info("robot result: " + responseEntity.getBody());
        }
    }

    @Override
    public Expression getDefaultTestSuiteTemplate() {
        return null;
    }

    @Override
    public Expression getDefaultProjectSummaryTemplate() {
        return null;
    }

    @Override
    public Expression getDefaultDeviceMessageTemplate() {
        return null;
    }
}
