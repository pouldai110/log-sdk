package cn.rivamed.log.springboot.configuration;

import cn.rivamed.log.core.spring.RivamedLogPropertyInit;
import cn.rivamed.log.springboot.property.RivamedLogProperty;
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
@EnableConfigurationProperties(RivamedLogProperty.class)
@AutoConfigureAfter(RivamedLogCommonAutoConfiguration.class)
@PropertySource(
        name = "Rivamed Log Default Properties",
        value = "classpath:/META-INF/rivamed-log-default.properties")
public class RivamedLogPropertyConfiguration {

    @Bean
    @ConditionalOnMissingBean(RivamedLogPropertyInit.class)
    public RivamedLogPropertyInit rivamedLogPropertyInit(RivamedLogProperty rivamedLogProperty) {
        RivamedLogPropertyInit rivamedLogPropertyInit = new RivamedLogPropertyInit();
        rivamedLogPropertyInit.setHost(rivamedLogProperty.getHost())
                .setPort(rivamedLogProperty.getPort())
                .setVirtualHost(rivamedLogProperty.getVirtualHost())
                .setUsername(rivamedLogProperty.getUsername())
                .setPassword(rivamedLogProperty.getPassword())
                .setExchange(rivamedLogProperty.getExchange())
                .setRoutingKey(rivamedLogProperty.getRoutingKey())
                .setQueueName(rivamedLogProperty.getQueueName())
                .setSqlEnable(rivamedLogProperty.isSqlEnable())
                .setRabbitmqEnable(rivamedLogProperty.isRabbitmqEnable())
                .setTaskEnable(rivamedLogProperty.isTaskEnable())
                .setRequestEnable(rivamedLogProperty.isRequestEnable())
                .setResponseEnable(rivamedLogProperty.isResponseEnable());
        return rivamedLogPropertyInit;
    }
}
