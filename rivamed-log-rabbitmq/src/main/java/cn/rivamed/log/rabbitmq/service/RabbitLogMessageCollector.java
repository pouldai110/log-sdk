package cn.rivamed.log.rabbitmq.service;

import cn.rivamed.log.core.entity.RabbitLogMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author pujian
 * @date 2022/10/17 9:33
 */
@FunctionalInterface
public interface RabbitLogMessageCollector {

    /**
     * 收集MQ发送的消息数据
     *
     * @param rabbitTemplate rabbitTemplate
     * @param args           args
     * @return cn.rivamed.common.aspec.rabbit.MQData
     * @author pujian
     * @date 2022/10/13 10:54
     */
    RabbitLogMessage collect(RabbitTemplate rabbitTemplate, Object[] args);

}
