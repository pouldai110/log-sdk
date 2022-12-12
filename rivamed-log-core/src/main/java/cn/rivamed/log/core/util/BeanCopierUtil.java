package cn.rivamed.log.core.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述: Bean对象复制工具类
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @author 左健宏
 * @version V2.0.1
 * @date 22/12/06 9:26
 */
public class BeanCopierUtil {
    /**
     * 创建过的BeanCopier实例放到缓存中，下次可以直接获取，提升性能
     */
    private static final Map<CopierIdentity, BeanCopierPlus> BEAN_COPIERS = new ConcurrentHashMap<>();

    /**
     * 该方法没有自定义Converter,简单进行常规属性拷贝
     *
     * @param srcObj  源对象
     * @param destObj 目标对象
     */
    public static void copy(final Object srcObj, final Object destObj) {
        if (Objects.isNull(srcObj) || Objects.isNull(destObj)) {
            throw new RuntimeException("参数为空");
        }

        getCopier(srcObj.getClass(), destObj.getClass(), true).copy(srcObj, destObj, new SkipNullConverter());
    }

    // destClass 必须有无参构造器
    public static <T> T copy(final Object srcObj, final Class<T> destClass) {
        if (Objects.isNull(srcObj) || Objects.isNull(destClass)) {
            throw new RuntimeException("参数为空");
        }

        T t;
        try {
            t = destClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        getCopier(srcObj.getClass(), destClass, false).copy(srcObj, t, null);

        return t;
    }

    public static void copyWithNull(final Object srcObj, final Object destObj) {
        if (Objects.isNull(srcObj) || Objects.isNull(destObj)) {
            throw new RuntimeException("参数为空");
        }

        getCopier(srcObj.getClass(), destObj.getClass(), false).copy(srcObj, destObj, null);
    }

    private static BeanCopierPlus getCopier(Class<?> source, Class<?> target, boolean converter) {
        CopierIdentity key = new CopierIdentity(source, target);
        BeanCopierPlus copier;
        if (BEAN_COPIERS.containsKey(key)) {
            copier = BEAN_COPIERS.get(key);
        } else {
            copier = BeanCopierPlus.create(source, target, converter);
            BEAN_COPIERS.put(key, copier);
        }
        return copier;
    }

    @Data
    @AllArgsConstructor
    private static class CopierIdentity {
        private Class<?> source;
        private Class<?> target;
    }

    private static class SkipNullConverter implements CopyConverter {

        @Override
        public Object convert(Object sourceFiled, Class<?> targetFiledClass, Object targetFiledSetter, Object targetFiled) {
            return sourceFiled == null ? targetFiled : sourceFiled;
        }
    }
}
