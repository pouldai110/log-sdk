/*
package cn.rivamed.log.log4j.plugin;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.lookup.AbstractLookup;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

*/
/**
 * 描述: 用于log4j配置中心加载完成后获取配置用
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @author 左健宏
 * @version V2.0.1
 * @date 22/10/13 17:30
 *//*

@Plugin(name = "spring", category = StrLookup.CATEGORY)
public class MyApplicationContextInitializer extends AbstractLookup implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static Environment environment;


    @Override
    public String lookup(final LogEvent event, final String key) {
        if (environment != null) {
            return environment.getProperty(key);
        } else {
            return null;
        }
    }


    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        if (configurableApplicationContext instanceof AnnotationConfigServletWebServerApplicationContext) {
            environment = configurableApplicationContext.getEnvironment();
        }
    }


}
*/
