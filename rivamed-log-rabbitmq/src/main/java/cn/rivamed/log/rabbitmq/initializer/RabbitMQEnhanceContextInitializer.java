package cn.rivamed.log.rabbitmq.initializer;

import cn.rivamed.log.rabbitmq.instrument.RabbitMQInstrumentation;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 描述: RabbitMQ初始执行
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @author 左健宏
 * @version V2.0.1
 * @date 22/10/26 10:14
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RabbitMQEnhanceContextInitializer implements ApplicationContextInitializer {

    private static boolean sendEnhanceFlag = false;
    private static boolean receiveEnhanceFlag = false;

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        if (!sendEnhanceFlag) {
            try {
                sendEnhanceFlag = RabbitMQInstrumentation.sendEnhance();
            } catch (NotFoundException e) {
                e.printStackTrace();
            } catch (CannotCompileException e) {
                e.printStackTrace();
            }
        }

        if (!receiveEnhanceFlag) {
            try {
                receiveEnhanceFlag = RabbitMQInstrumentation.receiveEnhance();
            } catch (NotFoundException e) {
                e.printStackTrace();
            } catch (CannotCompileException e) {
                e.printStackTrace();
            }
        }

    }
}
