package cn.rivamed.log.springboot.configuration;

import cn.rivamed.log.web.RivamedLogWebConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Rivamed Log web层面的自动装配类
 *
 * @author Zuo Yang
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass(name = {"org.springframework.web.servlet.config.annotation.WebMvcConfigurer", "org.springframework.boot.web.servlet.FilterRegistrationBean"})
public class RivamedLogWebAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(RivamedLogWebConfig.class)
    public RivamedLogWebConfig tLogWebConfig(){
        return new RivamedLogWebConfig();
    }

    //目前先屏蔽掉打印body的功能
    /*@Bean
    public FilterRegistrationBean<ReplaceStreamFilter> filterRegistration() {
        FilterRegistrationBean<ReplaceStreamFilter> registration = new FilterRegistrationBean<>();
        // 设置自定义拦截器
        registration.setFilter(new ReplaceStreamFilter());
        // 设置拦截路径
        registration.addUrlPatterns("/*");
        // 设置优先级（保证tlog过滤器最先执行）
        registration.setOrder(-999);
        return registration;
    }*/
}
