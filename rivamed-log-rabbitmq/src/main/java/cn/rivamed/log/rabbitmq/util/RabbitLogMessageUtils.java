package cn.rivamed.log.rabbitmq.util;

import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.entity.RabbitLogMessage;
import cn.rivamed.log.core.spring.RivamedLogApplicationContextHolder;
import cn.rivamed.log.rabbitmq.instrument.RabbitMQInstrumentation;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.impl.AMQConnection;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

/**
 * @author Zuo Yang
 * @date 2022/10/13 10:34
 */
public class RabbitLogMessageUtils {

    /**
     * 从{@link RabbitTemplate}的convertAndSend和send方法收集消息数据
     *
     * @param rabbitTemplate rabbitTemplate
     * @param args           args
     * @return cn.rivamed.log.core.entity.RabbitLogMessage
     * @author Zuo Yang
     * @date 2022/10/17 10:08
     */
    public static RabbitLogMessage collectFromSend(RabbitTemplate rabbitTemplate, Object[] args) {
        String exchange = (String) args[0];
        String routingKey = (String) args[1];
        Message message = (Message) args[2];
        CorrelationData correlationData = (CorrelationData) args[3];
        Object msg = rabbitTemplate.getMessageConverter().fromMessage(message);
        RabbitLogMessage rabbitLogMessage = new RabbitLogMessage();
        rabbitLogMessage.setVhost(rabbitTemplate.getConnectionFactory().getVirtualHost())
                .setExchange(exchange)
                .setRoutingKey(routingKey)
                .setMessage(msg)
                .setMessageId(correlationData == null ? null : correlationData.getId())
                .setMqType(LogMessageConstant.MESSAGE_TYPE_SEND)
                .setMethod(RabbitMQInstrumentation.ENHANCE_SEND_METHOD)
                .setClassName(RabbitMQInstrumentation.ENHANCE_RABBIT_TEMPLATE_CLASS);
        return rabbitLogMessage;
    }


    /**
     * @param message
     * @return cn.rivamed.log.core.entity.RabbitLogMessage
     * @author Zuo Yang
     * @date 2022/10/17 9:58
     */
    public static RabbitLogMessage collectFromReceive(Message message) {
        Object msg = null;
        String vhost = null, exchange = null, routingKey = null, queue = null, messageId = null, className = null, methodName = null;
        if (message != null) {
            MessageProperties messageProperties = message.getMessageProperties();
            exchange = messageProperties.getReceivedExchange();
            routingKey = messageProperties.getReceivedRoutingKey();
            queue = messageProperties.getConsumerQueue();
            messageId = messageProperties.getMessageId();

            ApplicationContext applicationContext = RivamedLogApplicationContextHolder.getApplicationContext();
            RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
            if (rabbitTemplate != null) {
                vhost = rabbitTemplate.getConnectionFactory().getVirtualHost();
            }
            try {
                msg = new String(message.getBody(), StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Method targetMethod = message.getMessageProperties().getTargetMethod();
            if (null != targetMethod) {
                className = targetMethod.getDeclaringClass().getName();
                methodName = targetMethod.getName();
            }
        }
        RabbitLogMessage rabbitLogMessage = new RabbitLogMessage();
        rabbitLogMessage.setVhost(vhost)
                .setExchange(exchange)
                .setRoutingKey(routingKey)
                .setQueueName(queue)
                .setMessage(msg)
                .setMessageId(messageId)
                .setMqType(LogMessageConstant.MESSAGE_TYPE_ACCEPT)
                .setClassName(className)
                .setMethod(methodName);
        return rabbitLogMessage;
    }

}
