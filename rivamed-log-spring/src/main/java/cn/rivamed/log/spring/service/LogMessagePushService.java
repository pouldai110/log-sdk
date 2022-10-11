package cn.rivamed.log.spring.service;

import cn.rivamed.log.core.util.UuidUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 推送到RabbitMQ服务
 *
 * @author Zuo Yang
 * @date 2022/10/01
 */
@Service
public class LogMessagePushService {

    private static Logger logger = LoggerFactory.getLogger(LogMessagePushService.class);

    @Resource
    @Lazy
    private BatchingRabbitTemplate batchingRabbitTemplate;

    @Resource
    private ObjectMapper objectMapper;

    @Value("${rivamed.log.exchange}")
    private String exchange;

    @Value("${rivamed.log.routingKey}")
    private String routingKey;

    /**
     * sendMessage（综合消息发送）
     */
    public void sendMessage(Object logMessage) {
        try {
            if (StringUtils.isNotBlank(exchange) && StringUtils.isNotBlank(routingKey)) {

                String messageId = UuidUtil.generatorUuid();
                MessageProperties messageProperties = new MessageProperties();

                // 设置messageId，给消费端做幂等性
                messageProperties.setMessageId(messageId);
                // 生成消息对象
                MessageConverter messageConverter = batchingRabbitTemplate.getMessageConverter();
                Message message = messageConverter.toMessage(objectMapper.writeValueAsBytes(logMessage), messageProperties);
                System.out.println("收到消息：" + logMessage);
                batchingRabbitTemplate.send(exchange, routingKey, message, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
