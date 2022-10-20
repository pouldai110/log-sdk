package cn.rivamed.log.rabbitmq.util;

import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.entity.RabbitLogMessage;
import cn.rivamed.log.core.spring.RivamedLogApplicationContextHolder;
import cn.rivamed.log.rabbitmq.service.RabbitLogMessageCollector;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author pujian
 * @date 2022/10/13 10:34
 */
public class RabbitLogMessageUtils {

    static {
        collectorCache = new ConcurrentHashMap<>();
        try {
            addCollector();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static ConcurrentHashMap<Method, RabbitLogMessageCollector> collectorCache;

    /**
     * 添加收集器
     *
     * @author pujian
     * @date 2022/10/17 10:12
     */
    private static void addCollector() throws NoSuchMethodException {
        Method send = RabbitTemplate.class.getDeclaredMethod("send", Message.class);
        RabbitLogMessageCollector rabbitLogMessageCollector = (rabbitTemplate, args) -> {
            Message message = (Message) args[0];
            Object msg = rabbitTemplate.getMessageConverter().fromMessage(message);
            return new RabbitLogMessage()
                    .setVhost(rabbitTemplate.getConnectionFactory().getVirtualHost())
                    .setExchange(rabbitTemplate.getExchange())
                    .setRoutingKey(rabbitTemplate.getRoutingKey())
                    .setMessage(msg)
                    .setMqType(LogMessageConstant.MESSAGE_TYPE_SEND);
        };
        addCollector(send, rabbitLogMessageCollector);

        send = RabbitTemplate.class.getDeclaredMethod("send", String.class, Message.class);
        rabbitLogMessageCollector = (rabbitTemplate, args) -> {
            String routingKey = (String) args[0];
            Message message = (Message) args[1];
            Object msg = rabbitTemplate.getMessageConverter().fromMessage(message);
            return new RabbitLogMessage()
                    .setVhost(rabbitTemplate.getConnectionFactory().getVirtualHost())
                    .setExchange(rabbitTemplate.getExchange())
                    .setRoutingKey(routingKey)
                    .setMessage(msg)
                    .setMqType(LogMessageConstant.MESSAGE_TYPE_SEND);
        };
        addCollector(send, rabbitLogMessageCollector);

        send = RabbitTemplate.class.getDeclaredMethod("send", String.class, String.class, Message.class);
        rabbitLogMessageCollector = (rabbitTemplate, args) -> {
            String exchange = (String) args[0];
            String routingKey = (String) args[1];
            Message message = (Message) args[2];
            Object msg = rabbitTemplate.getMessageConverter().fromMessage(message);
            return new RabbitLogMessage()
                    .setVhost(rabbitTemplate.getConnectionFactory().getVirtualHost())
                    .setExchange(exchange)
                    .setRoutingKey(routingKey)
                    .setMessage(msg)
                    .setMqType(LogMessageConstant.MESSAGE_TYPE_SEND);
        };
        addCollector(send, rabbitLogMessageCollector);
        send = RabbitTemplate.class.getDeclaredMethod("send", String.class, String.class, Message.class, CorrelationData.class);
        rabbitLogMessageCollector = (rabbitTemplate, args) -> {
            String exchange = (String) args[0];
            String routingKey = (String) args[1];
            Message message = (Message) args[2];
            CorrelationData correlationData = (CorrelationData) args[3];
            Object msg = rabbitTemplate.getMessageConverter().fromMessage(message);
            return new RabbitLogMessage()
                    .setVhost(rabbitTemplate.getConnectionFactory().getVirtualHost())
                    .setExchange(exchange)
                    .setRoutingKey(routingKey)
                    .setMessage(msg)
                    .setMessageId(correlationData.getId())
                    .setMqType(LogMessageConstant.MESSAGE_TYPE_SEND);
        };
        addCollector(send, rabbitLogMessageCollector);

        Method convertAndSend = RabbitTemplate.class.getDeclaredMethod("convertAndSend", Object.class);
        rabbitLogMessageCollector = (rabbitTemplate, args) -> {
            Object msg = args[0];
            return new RabbitLogMessage()
                    .setVhost(rabbitTemplate.getConnectionFactory().getVirtualHost())
                    .setExchange(rabbitTemplate.getExchange())
                    .setRoutingKey(rabbitTemplate.getRoutingKey())
                    .setMessage(msg)
                    .setMqType(LogMessageConstant.MESSAGE_TYPE_SEND);
        };
        addCollector(convertAndSend, rabbitLogMessageCollector);

        convertAndSend = RabbitTemplate.class.getDeclaredMethod("convertAndSend", String.class, Object.class);
        rabbitLogMessageCollector = (rabbitTemplate, args) -> {
            String routingKey = (String) args[0];
            Object msg = args[1];
            return new RabbitLogMessage()
                    .setVhost(rabbitTemplate.getConnectionFactory().getVirtualHost())
                    .setExchange(rabbitTemplate.getExchange())
                    .setRoutingKey(routingKey)
                    .setMessage(msg)
                    .setMqType(LogMessageConstant.MESSAGE_TYPE_SEND);
        };
        addCollector(convertAndSend, rabbitLogMessageCollector);

        convertAndSend = RabbitTemplate.class.getDeclaredMethod("convertAndSend", String.class, Object.class, CorrelationData.class);
        rabbitLogMessageCollector = (rabbitTemplate, args) -> {
            String routingKey = (String) args[0];
            Object msg = args[1];
            CorrelationData correlationData = (CorrelationData) args[2];
            return new RabbitLogMessage()
                    .setVhost(rabbitTemplate.getConnectionFactory().getVirtualHost())
                    .setExchange(rabbitTemplate.getExchange())
                    .setRoutingKey(routingKey)
                    .setMessage(msg)
                    .setMqType(LogMessageConstant.MESSAGE_TYPE_SEND)
                    .setMessageId(correlationData.getId());
        };
        addCollector(convertAndSend, rabbitLogMessageCollector);


        convertAndSend = RabbitTemplate.class.getDeclaredMethod("convertAndSend", String.class, String.class, Object.class);
        rabbitLogMessageCollector = (rabbitTemplate, args) -> {
            String exchange = (String) args[0];
            String routingKey = (String) args[1];
            Object msg = args[2];
            return new RabbitLogMessage()
                    .setVhost(rabbitTemplate.getConnectionFactory().getVirtualHost())
                    .setExchange(exchange)
                    .setRoutingKey(routingKey)
                    .setMessage(msg)
                    .setMqType(LogMessageConstant.MESSAGE_TYPE_SEND);
        };
        addCollector(convertAndSend, rabbitLogMessageCollector);
        convertAndSend = RabbitTemplate.class.getDeclaredMethod("convertAndSend", String.class, String.class, Object.class, CorrelationData.class);
        rabbitLogMessageCollector = (rabbitTemplate, args) -> {
            String exchange = (String) args[0];
            String routingKey = (String) args[1];
            Object msg = args[2];
            CorrelationData correlationData = (CorrelationData) args[3];
            return new RabbitLogMessage()
                    .setVhost(rabbitTemplate.getConnectionFactory().getVirtualHost())
                    .setExchange(exchange)
                    .setRoutingKey(routingKey)
                    .setMessage(msg)
                    .setMqType(LogMessageConstant.MESSAGE_TYPE_SEND)
                    .setMessageId(correlationData.getId());
        };
        addCollector(convertAndSend, rabbitLogMessageCollector);
    }

    /**
     * 添加收集器
     *
     * @param method        method
     * @param dataCollector dataCollector
     * @author pujian
     * @date 2022/10/17 9:59
     */
    public static void addCollector(Method method, RabbitLogMessageCollector dataCollector) {
        Assert.notNull(method, "method can't be null");
        Assert.notNull(dataCollector, "dataCollector can't be null");
        collectorCache.put(method, dataCollector);
    }


    /**
     * 从{@link RabbitTemplate}的convertAndSend和send方法收集消息数据
     *
     * @param rabbitTemplate rabbitTemplate
     * @param method         method
     * @param args           args
     * @return cn.rivamed.common.aspec.rabbit.RabbitLogMessage
     * @author pujian
     * @date 2022/10/17 10:08
     */
    public static RabbitLogMessage collectFromSend(RabbitTemplate rabbitTemplate, Method method, Object[] args) {
        RabbitLogMessageCollector rabbitLogMessageCollector = collectorCache.get(method);
        if (rabbitLogMessageCollector == null) {
            return null;
        }
        return rabbitLogMessageCollector.collect(rabbitTemplate, args);
    }


    /**
     * 从{@link org.springframework.amqp.rabbit.annotation.RabbitListener}标注的方法收集消息数据
     *
     * @param args args
     * @return cn.rivamed.common.aspec.rabbit.RabbitLogMessage
     * @author pujian
     * @date 2022/10/17 9:58
     */
    public static RabbitLogMessage collectFromListener(Object[] args) {
        Message message = null;
        Object msg = null;
        for (Object arg : args) {
            if (arg instanceof Channel) {
                // ignore
            } else if (arg instanceof Message) {
                message = (Message) arg;
            } else if (arg instanceof byte[]) {
                msg = new String((byte[]) arg);
            } else {
                msg = arg;
            }
        }
        String vhost = null, exchange = null, routingKey = null, queue = null;
        if (message != null) {
            MessageProperties messageProperties = message.getMessageProperties();
            exchange = messageProperties.getReceivedExchange();
            routingKey = messageProperties.getReceivedRoutingKey();
            queue = messageProperties.getConsumerQueue();
            if (msg == null) {
                ApplicationContext applicationContext = RivamedLogApplicationContextHolder.getApplicationContext();
                RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
                if (rabbitTemplate != null) {
                    msg = rabbitTemplate.getMessageConverter().fromMessage(message);
                    vhost = rabbitTemplate.getConnectionFactory().getVirtualHost();
                }
            }
        }
        return new RabbitLogMessage()
                .setVhost(vhost)
                .setExchange(exchange)
                .setRoutingKey(routingKey)
                .setMessage(msg)
                .setMqType(LogMessageConstant.MESSAGE_TYPE_ACCEPT);
    }

}
