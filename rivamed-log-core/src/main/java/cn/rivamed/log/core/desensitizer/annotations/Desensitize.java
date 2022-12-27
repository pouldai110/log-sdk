package cn.rivamed.log.core.desensitizer.annotations;

/**
 * 描述: jackson脱敏注解
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @version V2.10.0
 * @author: 左健宏
 * @date: 2022/12/26 10:47
 */

import cn.rivamed.log.core.desensitizer.enums.DesensitizeStrategy;
import cn.rivamed.log.core.desensitizer.handler.DesensitizeJsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JsonSerialize(using = DesensitizeJsonSerialize.class)
public @interface Desensitize {

    DesensitizeStrategy strategy() default DesensitizeStrategy.DEFAULT;
}
