package cn.rivamed.log.logback.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import cn.rivamed.log.core.entity.BaseLogMessage;
import cn.rivamed.log.core.factory.MessageAppenderFactory;
import cn.rivamed.log.core.rabbitmq.RabbitMQClient;
import cn.rivamed.log.logback.util.LogMessageUtil;

/**
 * className：RabbitMQAppender
 * description：
 * time：2022-10-09.15:26
 *
 * @author Zuo Yang
 * @version 1.0.0
 */
public class RabbitMQAppender extends AppenderBase<ILoggingEvent> {

    @Override
    protected void append(ILoggingEvent event) {
        if (RabbitMQClient.getClient() != null) {
            final BaseLogMessage logMessage = LogMessageUtil.getLogMessage(event);
            MessageAppenderFactory.push(logMessage);
        }
    }

    @Override
    public void start() {
        super.start();
    }
}
