package com.sonic.transport.receiver;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.sonic.common.http.RespModel;
import com.sonic.transport.feign.ControllerFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedHashMap;

@Component
public class TestDataReceiver {
    private final Logger logger = LoggerFactory.getLogger(TestDataReceiver.class);
    @Autowired
    private ControllerFeignClient controllerFeignClient;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "TestDataQueue")
    public void process(JSONObject jsonMsg, Channel channel, Message message) throws Exception {
        logger.info("TestDataQueue消费者收到消息  : " + jsonMsg.toString());
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        RespModel controllerResp = null;
        switch (jsonMsg.getString("msg")) {
            case "auth":
                controllerResp = controllerFeignClient.auth(jsonMsg.getString("key"));
                if (controllerResp != null
                        && controllerResp.getCode() == 2000
                        && (Integer) controllerResp.getData() != 0) {
                    JSONObject auth = new JSONObject();
                    auth.put("msg", "auth");
                    auth.put("id", controllerResp.getData());
                    rabbitTemplate.convertAndSend("MsgDirectExchange", jsonMsg.getString("key"), auth);
                }
                break;
            case "agentInfo":
                jsonMsg.remove("msg");
                controllerResp = controllerFeignClient.saveAgent(jsonMsg);
                break;
            case "offLine":
                controllerResp = controllerFeignClient.offLine(jsonMsg.getInteger("agentId"));
                break;
            case "subResultCount":
                controllerResp = controllerFeignClient.subResultCount(jsonMsg.getInteger("rid"));
                break;
            case "deviceDetail":
                controllerResp = controllerFeignClient.deviceStatus(jsonMsg);
                break;
            case "elapsed":
                jsonMsg.remove("msg");
                controllerResp = controllerFeignClient.saveElapsed(jsonMsg);
                break;
            case "step":
            case "perform":
            case "record":
            case "status":
                controllerResp = controllerFeignClient.saveResultDetail(jsonMsg);
                break;
            case "findSteps":
                controllerResp = controllerFeignClient.findSteps(jsonMsg.getInteger("caseId"));
                if (controllerResp != null
                        && controllerResp.getCode() == 2000) {
                    LinkedHashMap d = (LinkedHashMap) controllerResp.getData();
                    JSONObject steps = new JSONObject();
                    steps.put("msg", "runStep");
                    steps.put("pf", d.get("pf"));
                    steps.put("steps", d.get("steps"));
                    steps.put("gp", d.get("gp"));
                    steps.put("sessionId", jsonMsg.getString("sessionId"));
                    steps.put("pwd", jsonMsg.getString("pwd"));
                    steps.put("udId", jsonMsg.getString("udId"));
                    rabbitTemplate.convertAndSend("MsgDirectExchange", jsonMsg.getString("key"), steps);
                }
                break;
        }
        if (controllerResp != null && controllerResp.getCode() == 2000) {
            channel.basicAck(deliveryTag, true);
        } else {
            channel.basicNack(deliveryTag, false, false);
            throw new Exception("");
        }
    }
}