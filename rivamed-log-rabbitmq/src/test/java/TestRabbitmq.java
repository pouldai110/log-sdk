import cn.rivamed.log.core.entity.BaseLogMessage;
import cn.rivamed.log.core.util.UuidUtil;
import cn.rivamed.log.rabbitmq.instrument.RabbitMQInstrumentation;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

import java.lang.reflect.InvocationTargetException;

/**
 * 描述: TODO
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @author 左健宏
 * @version V2.0.1
 * @date 22/10/24 17:38
 */
public class TestRabbitmq {
    private static final String ENHANCE_CLASS = "org.springframework.amqp.rabbit.core.RabbitTemplate"; // 增强的 client
    private static final String ENHANCE_METHOD = "send"; // 增强的方法

    public static MessageConverter messageConverter() {
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(om);
    }

    public static void main(String[] args) throws NotFoundException, CannotCompileException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        RabbitMQInstrumentation.sendEnhance();
        RabbitMQInstrumentation.receiveEnhance();

        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setHost("192.168.111.222");
        cachingConnectionFactory.setPort(5672);
        cachingConnectionFactory.setVirtualHost("jishou");
        cachingConnectionFactory.setUsername("rivamed");
        cachingConnectionFactory.setPassword("rivamed");
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(cachingConnectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UuidUtil.generatorUuid());
        BaseLogMessage baseLogMessage = new BaseLogMessage();
        baseLogMessage.setSpanId("111").setTraceId("222").setEnv("prod").setSysName("HVC");
        rabbitTemplate.convertAndSend("{\"traceId\":\"5032f7d87ac06f2e\",\"spanId\":\"5032f7d87ac06f2e}");
        rabbitTemplate.convertAndSend("log.rm-th1", "{\"traceId\":\"5032f7d87ac06f2e\",\"spanId\":\"5032f7d87ac06f2e}");
        rabbitTemplate.convertAndSend("log.rm-th1", (Object) "{\"traceId\":\"5032f7d87ac06f2e\",\"spanId\":\"5032f7d87ac06f2e}", correlationData);
        rabbitTemplate.convertAndSend("log.rm-th1", "log.rm-th1", "{\"traceId\":\"5032f7d87ac06f2e\",\"spanId\":\"5032f7d87ac06f2e}");
        rabbitTemplate.convertAndSend("log.rm-th1", "log.rm-th1", "{\"traceId\":\"5032f7d87ac06f2e\",\"spanId\":\"5032f7d87ac06f2e}", correlationData);
        rabbitTemplate.convertAndSend("log.rm-th1",baseLogMessage);
    }
}
