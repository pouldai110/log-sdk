package cn.rivamed.log.core.util;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    private static final Map<String, Boolean> commonClassCache = new ConcurrentHashMap<>(16);


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
            //增加缓存 防止解析时间过长影响到业务流程
            boolean present;
            if (commonClassCache.containsKey(className)) {
                present = commonClassCache.get(className);
            } else {
                present = ClassUtils.isPresent(className, null);
                commonClassCache.put(className, present);
            }
            if (present) {
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
