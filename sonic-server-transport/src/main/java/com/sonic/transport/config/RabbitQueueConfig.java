package com.sonic.transport.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ZhouYiXun
 * @des rabbitmq队列配置
 * @date 2021/8/23 19:26
 */
@Configuration
public class RabbitQueueConfig {
    private final Logger logger = LoggerFactory.getLogger(RabbitQueueConfig.class);

    @Bean("PackageExchange")
    public FanoutExchange PackageExchange() {
        return new FanoutExchange("PackageExchange", true, false);
    }

    @Bean("MsgDirectExchange")
    public DirectExchange MsgDirectExchange() {
        return new DirectExchange("MsgDirectExchange", true, false);
    }

    @Bean("AgentExchange")
    public FanoutExchange AgentExchange() {
        return new FanoutExchange("AgentExchange", true, false);
    }

    @Bean("DataExchange")
    public DirectExchange DataExchange() {
        return new DirectExchange("DataExchange", true, false);
    }

    @Bean("TestDataQueue")
    public Queue TestDataQueue() {
        return new Queue("TestDataQueue", true);
    }

    @Bean
    public Binding bindingDirect(@Qualifier("TestDataQueue") Queue queue,
                                 @Qualifier("DataExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("data");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                logger.error("ConfirmCallback: 相关数据：" + correlationData + "\n" +
                        "确认情况：" + false + "\n" +
                        "原因：" + cause);
            }
        });

        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) ->
                logger.info("ConfirmCallback: 消息：" + message + "\n" +
                        "回应码：" + replyCode + "\n" +
                        "回应信息：" + replyText + "\n" +
                        "交换机：" + exchange + "\n" +
                        "路由键：" + routingKey));

        return rabbitTemplate;
    }

}
