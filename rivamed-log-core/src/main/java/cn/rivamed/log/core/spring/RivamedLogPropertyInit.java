package cn.rivamed.log.core.spring;

import cn.rivamed.log.core.client.AbstractClient;
import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.context.RivamedLogContext;
import cn.rivamed.log.core.enums.RivamedLogQueueEnum;
import cn.rivamed.log.core.rabbitmq.LogConfigTopicListener;
import cn.rivamed.log.core.rabbitmq.RabbitMQClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import java.text.MessageFormat;

/**
 * Rivamed Log参数初始化类，适用于springboot和spring
 *
 * @author Zuo Yang
 * @since 1.2.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RivamedLogPropertyInit implements InitializingBean {

    @Value("${spring.application.name}")
    private String sysName;

    @Value("${rivamed.log.clientIp}")
    private String clientIp;

    @Value("${server.port}")
    private String clientPort;

    private boolean logEnabled;
    private boolean sqlEnabled;
    private boolean rabbitmqEnabled;
    private boolean taskEnabled;
    private boolean requestEnabled;
    private boolean responseEnabled;
    private String host;
    private int port;
    private String virtualHost;
    private String username;
    private String password;

    @Override
    public void afterPropertiesSet() {

        RivamedLogContext.setSysName(sysName);
        RivamedLogContext.setLogEnabled(logEnabled);
        RivamedLogContext.setSqlEnabled(sqlEnabled);
        RivamedLogContext.setRabbitmqEnabled(rabbitmqEnabled);
        RivamedLogContext.setTaskEnabled(taskEnabled);
        RivamedLogContext.setRequestEnabled(requestEnabled);
        RivamedLogContext.setResponseEnabled(responseEnabled);
        //只有开启了日志打印的才注册事件 绑定队列
        if (logEnabled) {
            RabbitMQClient rabbitMQClient = RabbitMQClient.getInstance(host, port, virtualHost, username, password);
            AbstractClient.setClient(rabbitMQClient);
            RabbitAdmin admin = new RabbitAdmin(rabbitMQClient.getCachingConnectionFactory());
            //设置日志传输队列 自动创建交换机、路由、队列及绑定关系
            for (RivamedLogQueueEnum rivamedLogQueueEnum : RivamedLogQueueEnum.values()) {
                DirectExchange exchange = new DirectExchange(rivamedLogQueueEnum.getExchangeName(), true, false);
                Queue queue = new Queue(rivamedLogQueueEnum.getQueueName(), true);
                admin.declareExchange(exchange);
                admin.declareQueue(queue);
                admin.declareBinding(BindingBuilder.bind(queue) // 直接创建队列
                        .to(exchange) // 直接创建交换机
                        .with(rivamedLogQueueEnum.getRoutingKey())); // 指定路由Key
            }
            //创建日志系统配置项监听队列 用于监听服务器的配置 采用发布订阅模式
            String queueName = MessageFormat.format(LogMessageConstant.RIVAMED_LOG_CLIENT_QUEUE_NAME, sysName, clientIp, clientPort);
            Queue configQueue = new Queue(queueName, true, false, true);
            TopicExchange topicExchange = new TopicExchange(LogMessageConstant.RIVAMED_LOG_SERVER_EXCHANGE_NAME, true, true);
            admin.declareExchange(topicExchange);
            admin.declareQueue(configQueue);
            admin.declareBinding(BindingBuilder.bind(configQueue) // 直接创建队列
                    .to(topicExchange) // 直接创建交换机
                    .with(sysName)); // 指定路由Key
            //配置对应的监听器
            createMessageListenerContainer(rabbitMQClient.getCachingConnectionFactory(), configQueue);
        }
    }

    /**
     * 创建监听器
     *
     * @param cachingConnectionFactory
     * @param configQueue
     */
    private void createMessageListenerContainer(CachingConnectionFactory cachingConnectionFactory, Queue configQueue) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(cachingConnectionFactory);
        //加入所有的待消费队列到监听器内
        container.addQueues(configQueue);
        container.setExposeListenerChannel(true);
        //每个队列的消费者个数
        container.setConcurrentConsumers(1);
        //设置最大消费者个数---当消息堆积过多时候我们这里会自动增加消费者
        container.setMaxConcurrentConsumers(4);
        //设置每个消费者获取的最大的消息数量
        container.setPrefetchCount(1);
        //设置确认模式为手工确认
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //监听处理
        container.setMessageListener(new LogConfigTopicListener());
        container.start();
    }

}
