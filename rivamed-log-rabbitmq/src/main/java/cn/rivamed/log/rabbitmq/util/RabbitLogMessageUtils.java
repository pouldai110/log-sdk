package cn.rivamed.log.rabbitmq.util;

import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.entity.RabbitLogMessage;
import cn.rivamed.log.core.util.JsonUtil;
import cn.rivamed.log.rabbitmq.instrument.RabbitMQInstrumentation;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.connection.PublisherCallbackChannel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Zuo Yang
 * @date 2022/10/13 10:34
 */
public class RabbitLogMessageUtils {

    /**
     * 从{@link RabbitTemplate}的send方法收集消息数据
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
        String host = rabbitTemplate.getConnectionFactory().getHost();
        int port = rabbitTemplate.getConnectionFactory().getPort();
        Object msg = rabbitTemplate.getMessageConverter().fromMessage(message);
        RabbitLogMessage rabbitLogMessage = new RabbitLogMessage();
        rabbitLogMessage.setVhost(rabbitTemplate.getConnectionFactory().getVirtualHost())
                .setRabbitMQHost(host)
                .setRabbitMQPort(port)
                .setExchange(exchange)
                .setRoutingKey(routingKey)
                .setMessage(msg)
                .setMessageId(correlationData == null ? null : correlationData.getId())
                .setMqType(LogMessageConstant.MESSAGE_TYPE_SEND)
                .setMethod(RabbitMQInstrumentation.ENHANCE_RABBIT_TEMPLATE_CLASS + "." + RabbitMQInstrumentation.ENHANCE_SEND_METHOD)
                .setClassName(RabbitMQInstrumentation.ENHANCE_RABBIT_TEMPLATE_CLASS);
        return rabbitLogMessage;
    }


    /**
     * 获取消息基本参数
     *
     * @param message
     * @param channel
     * @return cn.rivamed.log.core.entity.RabbitLogMessage
     * @author Zuo Yang
     * @date 2022/10/17 9:58
     */
    public static RabbitLogMessage collectFromReceive(Message message, Channel channel) {
        String msg = null;
        String host = "127.0.0.1", vhost = "/", exchange = null, routingKey = null, queue = null, messageId = null, className = null, methodName = null;
        Integer port = 6379;
        if (message != null) {
            //获取队列修改信息
            MessageProperties messageProperties = message.getMessageProperties();
            exchange = messageProperties.getReceivedExchange();
            routingKey = messageProperties.getReceivedRoutingKey();
            queue = messageProperties.getConsumerQueue();
            //获取回调消息ID
            messageId = messageProperties.getHeader(PublisherCallbackChannel.RETURNED_MESSAGE_CORRELATION_KEY);
            host = channel.getConnection().getAddress().getHostAddress();
            port = channel.getConnection().getPort();
            //从channel里面拿virtualHost,如果长度超过3，取最后一个
            String[] strs = channel.getConnection().toString().split("/");
            if (strs.length > 3) {
                vhost = strs[3];
            }
            //获取消息
            try {
                msg = new String(message.getBody(), StandardCharsets.UTF_8.name());
                if (StringUtils.isBlank(messageId)) {
                    Map<String, String> msgMap = JsonUtil.json2Map(msg, String.class, String.class);
                    messageId = msgMap.get("messageId");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //获取listener的目标方法
            Method targetMethod = message.getMessageProperties().getTargetMethod();
            if (null != targetMethod) {
                className = targetMethod.getDeclaringClass().getName();
                methodName = targetMethod.getDeclaringClass().getSimpleName() + "." + targetMethod.getName();
            }
        }
        RabbitLogMessage rabbitLogMessage = new RabbitLogMessage();
        rabbitLogMessage
                .setRabbitMQHost(host)
                .setRabbitMQPort(port)
                .setVhost(vhost)
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
