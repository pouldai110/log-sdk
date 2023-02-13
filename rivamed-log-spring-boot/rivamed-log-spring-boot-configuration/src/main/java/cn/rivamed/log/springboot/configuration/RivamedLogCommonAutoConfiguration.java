package cn.rivamed.log.springboot.configuration;

import cn.rivamed.log.core.spring.RivamedLogApplicationContextHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("cn.rivamed.log")
public class RivamedLogCommonAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RivamedLogApplicationContextHolder.class)
    public RivamedLogApplicationContextHolder logApplicationContextHolder(){
        return new RivamedLogApplicationContextHolder();
    }

}
