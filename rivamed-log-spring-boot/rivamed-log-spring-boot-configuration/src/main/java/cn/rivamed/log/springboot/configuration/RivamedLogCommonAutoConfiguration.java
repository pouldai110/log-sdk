package cn.rivamed.log.springboot.configuration;

import cn.rivamed.log.core.spring.RivamedLogApplicationContextHolder;
import cn.rivamed.log.rabbitmq.aspect.RabbitAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RivamedLogCommonAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RivamedLogApplicationContextHolder.class)
    public RivamedLogApplicationContextHolder logApplicationContextHolder(){
        return new RivamedLogApplicationContextHolder();
    }

    @Bean
    @ConditionalOnMissingBean(RabbitAspect.class)
    public RabbitAspect rabbitAspect(){
        return new RabbitAspect();
    }
}
