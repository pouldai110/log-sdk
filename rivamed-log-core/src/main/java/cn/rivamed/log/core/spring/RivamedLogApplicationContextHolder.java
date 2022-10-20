package cn.rivamed.log.core.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author pujian
 * @date 2022/10/13 14:19
 */
public class RivamedLogApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    public static ApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException(RivamedLogApplicationContextHolder.class.getName() + ".applicationContext属性还未赋值");
        }
        return applicationContext;
    }

}
