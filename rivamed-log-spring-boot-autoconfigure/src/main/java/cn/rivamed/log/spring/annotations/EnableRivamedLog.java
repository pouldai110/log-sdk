package cn.rivamed.log.spring.annotations;

import cn.rivamed.log.spring.configuration.RivamedLogAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述: TODO
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @version V2.10.0
 * @author: Zuo Yang
 * @date: 2022/10/8 10:19
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({RivamedLogAutoConfiguration.class})
@ConditionalOnWebApplication
public @interface EnableRivamedLog {
}
