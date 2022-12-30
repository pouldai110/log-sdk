package cn.rivamed.log.core.spring;

import cn.rivamed.log.core.client.AbstractClient;
import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.context.RivamedLogContext;
import cn.rivamed.log.core.rabbitmq.LogConfigTopicListener;
import cn.rivamed.log.core.rabbitmq.RabbitMQClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
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

    private boolean sqlEnable;
    private boolean rabbitmqEnable;
    private boolean taskEnable;
    private boolean requestEnable;
    private boolean responseEnable;
    private String host;
    private int port;
    private String virtualHost;
    private String username;
    private String password;
    private String exchange;
    private String routingKey;
    private String queueName;

    @Override
    public void afterPropertiesSet() {
        RabbitMQClient rabbitMQClient = RabbitMQClient.getInstance(host, port, virtualHost, username, password, exchange, routingKey);
        AbstractClient.setClient(rabbitMQClient);
        RivamedLogContext.setSysName(sysName);
        RivamedLogContext.setSqlEnable(sqlEnable);
        RivamedLogContext.setRabbitmqEnable(rabbitmqEnable);
        RivamedLogContext.setTaskEnable(taskEnable);
        RivamedLogContext.setRequestEnable(requestEnable);
        RivamedLogContext.setResponseEnable(responseEnable);

        RabbitAdmin admin = new RabbitAdmin(rabbitMQClient.getCachingConnectionFactory());
        //设置日志传输队列 自动创建交换机、路由、队列及绑定关系
        if (StringUtils.isNotBlank(exchange) && StringUtils.isNotBlank(routingKey) && StringUtils.isNotBlank(queueName)) {
            Queue queue = new Queue(queueName, true);
            DirectExchange exchange1 = new DirectExchange(exchange, true, false);
            admin.declareExchange(exchange1);
            admin.declareQueue(queue);
            admin.declareBinding(BindingBuilder.bind(queue) // 直接创建队列
                    .to(exchange1) // 直接创建交换机
                    .with(routingKey)); // 指定路由Key
        }
        //先创建队列  用于和日志服务器推送客户端系统配置
        Queue clientQueue = new Queue(LogMessageConstant.RIVAMED_REG_LOG_QUEUE_NAME, true);
        admin.declareQueue(clientQueue);

        //创建登录日志队列  用于上传登录日志
        Queue loginLogQueue = new Queue(LogMessageConstant.RIVAMED_LOGIN_LOG_QUEUE_NAME, true);
        admin.declareQueue(loginLogQueue);

        //创建日志系统配置项监听队列 用于监听服务器的配置 采用发布订阅模式 队列名采用 sysName://ip:port 格式，方便查看
        Queue configQueue = new Queue(sysName + "://" + clientIp + ":" + clientPort, true, false, true);
        TopicExchange topicExchange = new TopicExchange(LogMessageConstant.RIVAMED_LOG_CONFIG, true, true);
        admin.declareExchange(topicExchange);
        admin.declareQueue(configQueue);
        admin.declareBinding(BindingBuilder.bind(configQueue) // 直接创建队列
                .to(topicExchange) // 直接创建交换机
                .with(sysName)); // 指定路由Key
        //配置对应的监听器
        createMessageListenerContainer(rabbitMQClient.getCachingConnectionFactory(), configQueue);
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
