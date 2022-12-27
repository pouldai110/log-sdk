package cn.rivamed.log.core.desensitizer.handler;

import cn.rivamed.log.core.desensitizer.annotations.Desensitize;
import cn.rivamed.log.core.desensitizer.enums.DesensitizeStrategy;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.util.Objects;

/**
 * 描述: jackson脱敏序列号
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @version V2.10.0
 * @author: 左健宏
 * @date: 2022/12/26 10:51
 */
public class DesensitizeJsonSerialize extends JsonSerializer<String> implements ContextualSerializer {

    private DesensitizeStrategy strategy;

    // 在序列化时进行数据脱敏
    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(strategy.desensitizeSerializer().apply(value));
    }

    // 获取注解上的属性
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property) throws JsonMappingException {
        Desensitize annotation = property.getAnnotation(Desensitize.class);
        if (Objects.nonNull(annotation) && Objects.equals(String.class, property.getType().getRawClass())) {
            // 主要代码在这里，获取脱敏的规则
            this.strategy = annotation.strategy();
            return this;
        }
        return provider.findValueSerializer(property.getType(), property);

    }
}

