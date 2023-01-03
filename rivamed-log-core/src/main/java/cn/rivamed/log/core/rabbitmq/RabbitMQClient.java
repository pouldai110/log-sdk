package cn.rivamed.log.core.rabbitmq;

import cn.rivamed.log.core.client.AbstractClient;
import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.enums.RivamedLogQueueEnum;
import cn.rivamed.log.core.util.UuidUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.batch.BatchingStrategy;
import org.springframework.amqp.rabbit.batch.SimpleBatchingStrategy;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import java.util.List;

/**
 * 描述: RabbitMQ 客户端
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @author 左健宏
 * @version V2.0.1
 * @date 22/10/11 10:50
 */
public class RabbitMQClient extends AbstractClient {

    private static RabbitMQClient instance;

    private CachingConnectionFactory cachingConnectionFactory;
    private BatchingRabbitTemplate batchingRabbitTemplate;
    private RabbitTemplate rabbitTemplate;

    public RabbitMQClient(String host, int port, String virtualHost, String username, String password) {
        BatchingStrategy strategy = new SimpleBatchingStrategy(1000, 25_0000, 10_000);
        TaskScheduler scheduler = new ConcurrentTaskScheduler();
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setHost(host);
        cachingConnectionFactory.setPort(port);
        cachingConnectionFactory.setVirtualHost(virtualHost);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        BatchingRabbitTemplate batchingRabbitTemplate = new BatchingRabbitTemplate(strategy, scheduler);
        batchingRabbitTemplate.setConnectionFactory(cachingConnectionFactory);
        batchingRabbitTemplate.setMessageConverter(messageConverter());

        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());

        this.cachingConnectionFactory = cachingConnectionFactory;
        this.batchingRabbitTemplate = batchingRabbitTemplate;
        this.rabbitTemplate = rabbitTemplate;

    }

    public MessageConverter messageConverter() {
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(om);
    }

    public static RabbitMQClient getInstance(String host, int port, String virtualHost, String username, String password) {
        if (instance == null) {
            synchronized (RabbitMQClient.class) {
                if (instance == null) {
                    instance = new RabbitMQClient(host, port, virtualHost, username, password);
                }
            }
        }
        return instance;
    }

    @Override
    public void pushMessage(String messageStr) {
        String messageId = UuidUtil.generatorUuid();
        MessageProperties messageProperties = new MessageProperties();
        // 设置messageId，给消费端做幂等性
        messageProperties.setMessageId(messageId);
        // 生成消息对象
        MessageConverter messageConverter = batchingRabbitTemplate.getMessageConverter();
        Message message = messageConverter.toMessage(messageStr, messageProperties);
        batchingRabbitTemplate.send(LogMessageConstant.RIVAMED_LOG_EXCHANGE_NAME, LogMessageConstant.RIVAMED_BUSINESS_LOG_ROUTING_KEY_NAME, message, null);
    }

    @Override
    public void pushSimpleMessage(RivamedLogQueueEnum rivamedLogQueueEnum, Object object) {
        rabbitTemplate.convertAndSend(rivamedLogQueueEnum.getExchangeName(), rivamedLogQueueEnum.getRoutingKey(), object);
    }

    @Override
    public void putMessageList(List<String> list) {
        for (String s : list) {
            pushMessage(s);
        }
    }

    public BatchingRabbitTemplate getBatchingRabbitTemplate() {
        return batchingRabbitTemplate;
    }

    public CachingConnectionFactory getCachingConnectionFactory() {
        return cachingConnectionFactory;
    }

}
