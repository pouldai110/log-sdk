package cn.rivamed.log.log4j2.autoconfigure;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

@Configuration
@ComponentScan("cn.rivamed.log")
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class RivamedLogAutoConfiguration {
}
