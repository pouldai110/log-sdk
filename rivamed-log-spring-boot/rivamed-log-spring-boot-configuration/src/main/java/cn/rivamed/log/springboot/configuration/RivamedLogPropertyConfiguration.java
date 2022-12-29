package cn.rivamed.log.springboot.configuration;

import cn.rivamed.log.core.spring.RivamedLogPropertyInit;
import cn.rivamed.log.springboot.property.RivamedLogProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

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

    protected static final Logger log = LoggerFactory.getLogger(RivamedLogPropertyConfiguration.class);

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

    /**
     * 配置和日志服务通信的 CachingConnectionFactory
     *
     * @param rivamedLogProperty
     * @return
     */
    @Bean("rivamedLogConnectionFactory")
    @DependsOn("rivamedLogPropertyInit")
    public CachingConnectionFactory rivamedLogConnectionFactory(RivamedLogProperty rivamedLogProperty) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername(rivamedLogProperty.getUsername());
        connectionFactory.setPassword(rivamedLogProperty.getPassword());
        connectionFactory.setVirtualHost(rivamedLogProperty.getVirtualHost());
        connectionFactory.setHost(rivamedLogProperty.getHost());
        connectionFactory.setPort(rivamedLogProperty.getPort());
        return connectionFactory;
    }

    /**
     * 配置和日志服务通信的 RabbitListenerContainerFactory
     *
     * @param configurer
     * @param connectionFactory
     * @return
     */
    @Bean("rivamedLogContainerFactory")
    @DependsOn("rivamedLogConnectionFactory")
    public SimpleRabbitListenerContainerFactory rivamedLogContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer,
                                                                           @Qualifier("rivamedLogConnectionFactory") ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        //初始化消费者数量
        factory.setConcurrentConsumers(2);
        //最大消费者数量
        factory.setMaxConcurrentConsumers(8);
        factory.setPrefetchCount(10);
        //自动确认消息
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setDefaultRequeueRejected(false);
        factory.setAdviceChain(RetryInterceptorBuilder
                        .stateless()
                        .recoverer(new RejectAndDontRequeueRecoverer())
                        .retryOperations(rivamedLogRetryTemplate())
                        .build()
        );
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean("rivamedLogRetryTemplate")
    @DependsOn("rivamedLogConnectionFactory")
    public RetryTemplate rivamedLogRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(backOffPolicy());
        retryTemplate.setRetryPolicy(retryPolicy());
        return retryTemplate;
    }

    @Bean("rivamedLogExponentialBackOffPolicy")
    @DependsOn("rivamedLogConnectionFactory")
    public ExponentialBackOffPolicy backOffPolicy() {
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(1000);
        backOffPolicy.setMaxInterval(10000);
        return backOffPolicy;
    }

    @Bean("rivamedLogSimpleRetryPolicy")
    @DependsOn("rivamedLogConnectionFactory")
    public SimpleRetryPolicy retryPolicy() {
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        return retryPolicy;
    }

}
