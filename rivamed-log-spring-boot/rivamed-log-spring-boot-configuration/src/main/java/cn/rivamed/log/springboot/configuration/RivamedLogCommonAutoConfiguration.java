package cn.rivamed.log.springboot.configuration;

import cn.rivamed.log.core.spring.RivamedLogApplicationContextHolder;
import cn.rivamed.log.task.spring.SpringScheduledTaskAop;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
    @ConditionalOnClass(org.springframework.scheduling.annotation.Scheduled.class)
    public SpringScheduledTaskAop springScheduledTaskAop(){
        return new SpringScheduledTaskAop();
    }

}
