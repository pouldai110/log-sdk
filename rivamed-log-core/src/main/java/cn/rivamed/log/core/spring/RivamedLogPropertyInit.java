package cn.rivamed.log.core.spring;

import cn.rivamed.log.core.client.AbstractClient;
import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.context.RivamedLogContext;
import cn.rivamed.log.core.entity.LogClientInfo;
import cn.rivamed.log.core.rabbitmq.RabbitMQClient;
import cn.rivamed.log.core.util.IpUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
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

        //发送客户端启动注册事件
        LogClientInfo logClientInfo = new LogClientInfo(sysName, IpUtil.getLocalHostIp(), clientPort);
        rabbitMQClient.pushSimpleMessage(LogMessageConstant.RIVAMED_REG_LOG_QUEUE_NAME, logClientInfo);

        //创建登录日志队列  用于上传登录日志
        Queue loginLogQueue = new Queue(LogMessageConstant.RIVAMED_LOGIN_LOG_QUEUE_NAME, true);
        admin.declareQueue(loginLogQueue);
    }

}
