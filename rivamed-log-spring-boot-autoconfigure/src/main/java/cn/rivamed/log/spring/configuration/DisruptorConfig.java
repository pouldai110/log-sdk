package cn.rivamed.log.spring.configuration;

import cn.rivamed.log.core.event.LogMessageEvent;
import cn.rivamed.log.spring.handler.LogMessageEventCleanHandler;
import cn.rivamed.log.spring.handler.LogMessageEventFactory;
import cn.rivamed.log.spring.handler.LogMessageEventHandler;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

/**
 * Disruptor配置
 *
 * @author Zuo Yang
 * @date 2020/12/10
 */
@Configuration
@EnableConfigurationProperties({RivamedLogProperties.class})
@ConditionalOnProperty(name = "rivamed.log.enable", havingValue = "true")
public class DisruptorConfig {

    @Bean
    public Disruptor disruptor(LogMessageEventHandler logMessageEventHandler) {

        LogMessageEventFactory eventFactory = new LogMessageEventFactory();
        // ringBuffer大小必须为2的倍数
        Disruptor<LogMessageEvent> disruptor = new Disruptor<>(eventFactory, 128 * 1024, new CustomizableThreadFactory("event-handler-"),
                ProducerType.SINGLE, new SleepingWaitStrategy());
        // 连接消费端
        disruptor.handleEventsWith(logMessageEventHandler).then(new LogMessageEventCleanHandler());
        disruptor.start();
        return disruptor;
    }
}
