package com.sonic.transport.receiver;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.sonic.common.http.RespModel;
import com.sonic.transport.feign.ControllerFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TestDataReceiver {
    private final Logger logger = LoggerFactory.getLogger(TestDataReceiver.class);
    @Autowired
    private ControllerFeignClient controllerFeignClient;

    @RabbitListener(queues = "TestDataQueue")
    public void process(JSONObject jsonMsg, Channel channel, Message message) throws IOException {
        logger.info("TestDataQueue消费者收到消息  : " + jsonMsg.toString());
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        RespModel controllerResp = null;
        switch (jsonMsg.getString("msg")) {
            case "agentInfo":
                jsonMsg.remove("msg");
                controllerResp = controllerFeignClient.saveAgent(jsonMsg);
                break;
            case "offLine":
                controllerResp = controllerFeignClient.offLine(jsonMsg.getInteger("id"));
                break;
            case "deviceDetail":
                controllerResp = controllerFeignClient.deviceStatus(jsonMsg);
                break;
        }
        if (controllerResp != null && controllerResp.getCode() == 2000) {
            channel.basicAck(deliveryTag, true);
        } else {
            channel.basicReject(deliveryTag, true);
        }
    }
}