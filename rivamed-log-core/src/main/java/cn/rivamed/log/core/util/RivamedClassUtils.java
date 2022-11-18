package cn.rivamed.log.core.util;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 描述:
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @author 左健宏
 * @version V2.0.1
 * @date 22/11/18 14:21
 */
public class RivamedClassUtils {

    /**
     * 获取方法的注解值
     *
     * @param className
     * @param method
     * @param field
     * @return
     */
    public static String getAnnotationValue(String className, Method method, String field) {
        String value = null;
        try {
            if (ClassUtils.isPresent(className, null)) {
                Class<Annotation> aClass = (Class<Annotation>) ClassUtils.forName(className, null);
                if (null != aClass) {
                    Annotation annotation = AnnotationUtils.findAnnotation(method, aClass);
                    if (annotation != null) {
                        value = String.valueOf(AnnotationUtils.getValue(annotation, field));
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }
}
