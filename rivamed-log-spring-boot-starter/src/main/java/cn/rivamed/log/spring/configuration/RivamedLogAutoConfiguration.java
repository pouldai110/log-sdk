package cn.rivamed.log.spring.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Rivamed Log AutoConfiguration
 *
 * @author Zuo Yang
 * @date 2022/10/01
 */
@Configuration
@EnableConfigurationProperties({RivamedLogProperties.class})
@ComponentScan(
        basePackages = {
                "cn.rivamed.log.spring",
        }
)
@ConditionalOnProperty(name = "rivamed.log.enable", havingValue = "true")
public class RivamedLogAutoConfiguration {

}
