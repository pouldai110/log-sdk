package cn.rivamed.log.core.util;

/**
 * 描述: Bean对象复制转换器
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @author 左健宏
 * @version V2.0.1
 * @date 22/12/06 9:27
 */
@FunctionalInterface
public interface CopyConverter {
    /**
     * @param sourceFiled       源属性
     * @param targetFiledClass  目标属性的class
     * @param targetFiledSetter 目标属性的setter方法的方法名
     * @param targetFiled       目标属性
     * @return 设置目标的属性
     */
    Object convert(Object sourceFiled, Class<?> targetFiledClass, Object targetFiledSetter, Object targetFiled);
}
