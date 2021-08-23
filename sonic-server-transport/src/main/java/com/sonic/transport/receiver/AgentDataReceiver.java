package com.sonic.transport.receiver;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AgentDataReceiver {
    private final Logger logger = LoggerFactory.getLogger(AgentDataReceiver.class);

    @RabbitListener(queues = "AgentDataQueue")
    public void process(JSONObject jsonMsg, Channel channel, Message message) throws IOException {
        logger.info("AgentDataQueue消费者收到消息  : " + jsonMsg.toString());
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        switch (jsonMsg.getString("msg")) {
        }
    }
}