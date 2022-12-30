package cn.rivamed.log.springboot.initializer;

import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.Properties;

/**
 * 描述: 自定义环境配置参数设置,将ip地址注册进环境变量供Rabbitmq Listener定义队列名称使用
 * 备注:
 *
 * @param
 * @author 左健宏
 * @date 22/12/07 11:05
 */
@Slf4j
public class RivamedLogEnvironmentPostPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

        //手动注册ip环境变量
        MutablePropertySources propertySources = environment.getPropertySources();
        PropertySource<?> systemProperties = propertySources.get("systemProperties");
        Properties systemPropertiesSource = (Properties) systemProperties.getSource();
        systemPropertiesSource.setProperty(LogMessageConstant.RIVAMED_LOG_CLIENT_IP, IpUtil.getLocalHostIp());
    }
}
