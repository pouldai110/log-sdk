package cn.rivamed.log.core.util;

import cn.rivamed.log.core.desensitizer.handler.DesensitizeJacksonAnnotationIntrospector;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * className：JsonUtil
 * description：json工具类
 * time：2022-10-01.16:17
 *
 * @author Zuo Yang
 * @version 1.0.0
 */
public class JsonUtil {

    protected static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final SimpleModule simpleModule = new SimpleModule();
    private static final JavaTimeModule javaTimeModule = new JavaTimeModule();


    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        // 允许对象忽略json中不存在的属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //属性名序列化策略：LOWER_CAMEL_CASE 除首个单词外，首字母大写
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        //设置脱敏
        objectMapper.setAnnotationIntrospector(new DesensitizeJacksonAnnotationIntrospector());
        //日期时间处理
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DateUtil.DATE_FORMAT_YYYY_MM_DD)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DateUtil.TIME_FORMAT_HH_MI_SS)));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DateUtil.DATE_FORMAT_YYYY_MM_DD)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DateUtil.TIME_FORMAT_HH_MI_SS)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS)));
        objectMapper.registerModule(javaTimeModule);
        //文件过滤
        simpleModule.addSerializer(MultipartFile.class, MultipartFileSerializer.INSTANCE);
        objectMapper.registerModule(simpleModule);
    }

    public static <T> T parseObject(String jsonString, Class<T> classz) {
        try {
            return (T) objectMapper.readValue(jsonString, classz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toJSONString(Object object) {
        if (object == null) {
            return null;
        }
        String result;
        try {
            result = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("toJSONString error", e);
            result = object.toString();
        }
        return result;
    }

    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, clazz);
        try {
            return objectMapper.readValue(json, javaType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <K, V> Map<K, V> json2Map(String jsonData, Class<K> keyType, Class<V> valueType) {
        JavaType javaType = objectMapper.getTypeFactory().constructMapType(Map.class, keyType, valueType);
        try {
            return objectMapper.readValue(jsonData, javaType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
