package cn.rivamed.log.core.spring;

import cn.rivamed.log.core.client.AbstractClient;
import cn.rivamed.log.core.context.RivamedLogContext;
import cn.rivamed.log.core.rabbitmq.RabbitMQClient;
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
    @Value("${spring.profiles.active:dev}")
    private String env;

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
        RivamedLogContext.setEnv(env);
        RivamedLogContext.setSqlEnable(sqlEnable);
        RivamedLogContext.setRabbitmqEnable(rabbitmqEnable);
        RivamedLogContext.setTaskEnable(taskEnable);
        RivamedLogContext.setRequestEnable(requestEnable);
        RivamedLogContext.setResponseEnable(responseEnable);
        //自动创建交换机、路由、队列及绑定关系
        if (StringUtils.isNotBlank(exchange) && StringUtils.isNotBlank(routingKey) && StringUtils.isNotBlank(queueName)) {
            RabbitAdmin admin = new RabbitAdmin(rabbitMQClient.getCachingConnectionFactory());
            Queue queue = new Queue(queueName, true);
            DirectExchange exchange1 = new DirectExchange(exchange, true, false);
            admin.declareExchange(exchange1);
            admin.declareQueue(queue);
            admin.declareBinding(BindingBuilder.bind(queue) // 直接创建队列
                    .to(exchange1) // 直接创建交换机
                    .with(routingKey)); // 指定路由Key
        }
    }

}
