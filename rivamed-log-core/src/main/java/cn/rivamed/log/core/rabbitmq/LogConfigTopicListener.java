package cn.rivamed.log.core.rabbitmq;

import cn.rivamed.log.core.context.RivamedLogContext;
import cn.rivamed.log.core.entity.RivamedLogProperty;
import cn.rivamed.log.core.spring.RivamedLogApplicationContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

import java.io.IOException;

/**
 * 描述: 日志系统配置项监听类  用于动态控制 各种日志是否打印
 * 备注:
 *
 * @param
 * @author 左健宏
 * @date 22/12/07 14:53
 */
public class LogConfigTopicListener implements ChannelAwareMessageListener {

    protected static final Logger log = LoggerFactory.getLogger(LogConfigTopicListener.class);

    private ObjectMapper objectMapper;

    /**
     * 采用发布订阅模式 队列名采用 sysName://ip:port 格式，方便查看
     * 服务器推送消息的时候按照sysName路由推送
     *
     * @param message
     * @param channel
     */
    @Override
    public void onMessage(Message message, Channel channel) throws IOException {
        try {
            if (objectMapper == null) {
                objectMapper = RivamedLogApplicationContextHolder.getApplicationContext().getBean(ObjectMapper.class);
            }
            log.info("-----收到日志服务器配置：{}", new String(message.getBody(), "UTF-8"));
            RivamedLogProperty rivamedLogProperty = objectMapper.readValue(message.getBody(), RivamedLogProperty.class);
            RivamedLogContext.setSqlEnabled(rivamedLogProperty.isSqlEnabled());
            RivamedLogContext.setRabbitmqEnabled(rivamedLogProperty.isRabbitmqEnabled());
            RivamedLogContext.setTaskEnabled(rivamedLogProperty.isTaskEnabled());
            RivamedLogContext.setRequestEnabled(rivamedLogProperty.isRequestEnabled());
            RivamedLogContext.setResponseEnabled(rivamedLogProperty.isResponseEnabled());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("-----日志服务器配置解析失败", e);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }
}
