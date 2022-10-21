package cn.rivamed.log.core.spring;

import cn.rivamed.log.core.client.AbstractClient;
import cn.rivamed.log.core.context.RivamedLogRecordContext;
import cn.rivamed.log.core.rabbitmq.RabbitMQClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
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
public class RivamedLogRabbitMQPropertyInit implements InitializingBean {

    @Value("${spring.application.name}")
    private String sysName;
    @Value("${spring.profiles.active:dev}")
    private String env;
    private String host;
    private int port;
    private String virtualHost;
    private String username;
    private String password;
    private String exchange;
    private String routingKey;

    @Override
    public void afterPropertiesSet() {
        RabbitMQClient rabbitMQClient = RabbitMQClient.getInstance(host, port, virtualHost, username, password, exchange, routingKey);
        AbstractClient.setClient(rabbitMQClient);
        RivamedLogRecordContext.setSysName(sysName);
        RivamedLogRecordContext.setEnv(env);
    }

}
