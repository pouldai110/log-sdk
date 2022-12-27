package cn.rivamed.log.core.desensitizer.handler;

import cn.rivamed.log.core.desensitizer.annotations.Desensitize;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

import java.lang.annotation.Annotation;

/**
 * 描述: TODO
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @version V2.10.0
 * @author: 左健宏
 * @date: 2022/12/27 16:09
 */
public class DesensitizeJacksonAnnotationIntrospector extends JacksonAnnotationIntrospector {

    private static final long serialVersionUID = 1L;

    @Override
    public boolean isAnnotationBundle(Annotation ann) {
        if (ann.annotationType() == Desensitize.class) {
            return true;
        } else {
            return super.isAnnotationBundle(ann);
        }
    }
}
