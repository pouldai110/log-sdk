package cn.rivamed.log.rabbitmq.interceptor;

import cn.rivamed.log.core.entity.RabbitLogMessage;
import cn.rivamed.log.core.factory.MessageAppenderFactory;
import cn.rivamed.log.rabbitmq.util.RabbitLogMessageUtils;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * 描述: RabbitMQ方法拦截解析类
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @author 左健宏
 * @version V2.0.1
 * @date 22/10/24 10:34
 */
public class RabbitMQInterceptor {

    /**
     * 处理RabbitMQ发送消息事件
     *
     * @param rabbitTemplate
     * @param args
     */
    public static void sendInterceptor(RabbitTemplate rabbitTemplate, Object[] args) {
        if (rabbitTemplate.getClass().isAssignableFrom(RabbitTemplate.class)) {
            RabbitLogMessage rabbitLogMessage = RabbitLogMessageUtils.collectFromSend(rabbitTemplate, args);
            MessageAppenderFactory.pushRabbitLogMessage(rabbitLogMessage);
        }

    }

    /**
     * 处理RabbitMQ接收消息事件
     *
     * @param message
     * @param channel
     */
    public static void receiveInterceptor(Message message, Channel channel) {
        RabbitLogMessage rabbitLogMessage = RabbitLogMessageUtils.collectFromReceive(message, channel);
        MessageAppenderFactory.pushRabbitLogMessage(rabbitLogMessage);
    }

}
