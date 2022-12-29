package cn.rivamed.log.springboot.listener;

import cn.rivamed.log.core.context.RivamedLogContext;
import cn.rivamed.log.springboot.property.RivamedLogProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 描述: 日志系统配置项监听类  用于动态控制 各种日志是否打印
 * 备注:
 *
 * @param
 * @author 左健宏
 * @date 22/12/07 14:53
 */
@Component
public class LogConfigTopicListener {

    protected static final Logger log = LoggerFactory.getLogger(LogConfigTopicListener.class);

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 采用发布订阅模式 队列名采用 sysName://ip:port 格式，方便查看
     * 服务器推送消息的时候按照sysName路由推送
     *
     * @param message
     * @param channel
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(
                    value = "#{'${spring.application.name}'}" + "://" + "#{'${rivamed.log.serverIp}'}" + ":" + "#{'${server.port}'}", durable = "true", autoDelete = "true"),
            exchange = @Exchange(value = "rivamed-log-config",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC,
                    autoDelete = "true"
            ),
            key = {"#{'${spring.application.name}'}"}
    ), containerFactory = "rivamedLogContainerFactory")
    public void logConfigTopicListener(Message message, Channel channel) throws IOException {
        try {
            log.info("-----收到日志服务器配置：{}", new String(message.getBody(), "UTF-8"));
            RivamedLogProperty rivamedLogProperty = objectMapper.readValue(message.getBody(), RivamedLogProperty.class);
            RivamedLogContext.setSqlEnable(rivamedLogProperty.isSqlEnable());
            RivamedLogContext.setRabbitmqEnable(rivamedLogProperty.isRabbitmqEnable());
            RivamedLogContext.setTaskEnable(rivamedLogProperty.isTaskEnable());
            RivamedLogContext.setRequestEnable(rivamedLogProperty.isRequestEnable());
            RivamedLogContext.setResponseEnable(rivamedLogProperty.isResponseEnable());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("-----日志服务器配置解析失败", e);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }
}
