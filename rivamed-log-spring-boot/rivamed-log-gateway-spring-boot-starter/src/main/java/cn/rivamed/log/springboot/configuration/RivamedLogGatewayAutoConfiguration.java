package cn.rivamed.log.springboot.configuration;

import cn.rivamed.log.gateway.filter.RivamedLogGatewayFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Zuo Yang
 * @since 1.0
 */
@Configuration
@ConditionalOnClass(name = {"org.springframework.cloud.gateway.filter.GlobalFilter"})
public class RivamedLogGatewayAutoConfiguration {
/*
    @Bean
    public RivamedLogGatewayFilter tLogGatewayFilter(){
        return new RivamedLogGatewayFilter();
    }*/
}
