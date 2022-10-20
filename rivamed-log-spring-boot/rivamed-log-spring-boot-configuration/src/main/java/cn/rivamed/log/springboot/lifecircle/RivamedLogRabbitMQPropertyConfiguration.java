package cn.rivamed.log.springboot.lifecircle;

import cn.rivamed.log.core.spring.RivamedLogRabbitMQPropertyInit;
import cn.rivamed.log.springboot.configuration.RivamedLogCommonAutoConfiguration;
import cn.rivamed.log.springboot.property.RivamedLogRabbitMQProperty;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Rivamed Log的参数自动装配类
 *
 * @author Zuo Yang
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(RivamedLogRabbitMQProperty.class)
@AutoConfigureAfter(RivamedLogCommonAutoConfiguration.class)
@PropertySource(
        name = "Rivamed Log Default Properties",
        value = "classpath:/META-INF/rivamed-log-default.properties")
public class RivamedLogRabbitMQPropertyConfiguration {

    @Bean
    @ConditionalOnMissingBean(RivamedLogRabbitMQPropertyInit.class)
    public RivamedLogRabbitMQPropertyInit rivamedLogRabbitMQPropertyInit(RivamedLogRabbitMQProperty rabbitMQProperty) {
        RivamedLogRabbitMQPropertyInit rabbitMQPropertyInit = new RivamedLogRabbitMQPropertyInit();
        rabbitMQPropertyInit.setHost(rabbitMQProperty.getHost())
                .setPort(rabbitMQProperty.getPort())
                .setVirtualHost(rabbitMQProperty.getVirtualHost())
                .setUsername(rabbitMQProperty.getUsername())
                .setPassword(rabbitMQProperty.getPassword())
                .setExchange(rabbitMQProperty.getExchange())
                .setRoutingKey(rabbitMQProperty.getRoutingKey());
        return rabbitMQPropertyInit;
    }
}
