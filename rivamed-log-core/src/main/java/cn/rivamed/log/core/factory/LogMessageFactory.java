package cn.rivamed.log.core.factory;

import cn.rivamed.log.core.client.AbstractClient;
import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.entity.LogRecordMessage;
import cn.rivamed.log.core.entity.LoginLogMessage;
import cn.rivamed.log.core.entity.RabbitLogMessage;
import cn.rivamed.log.core.entity.TraceId;
import cn.rivamed.log.core.enums.RivamedLogQueueEnum;
import cn.rivamed.log.core.rabbitmq.RabbitMQClient;
import cn.rivamed.log.core.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * className：logRecordMessageFactory
 * description： 日志工厂类
 * time：2022-10-03.14:04
 *
 * @author Zuo Yang
 * @version 1.0.0
 */
public class LogMessageFactory<T> {

    protected static final Logger log = LoggerFactory.getLogger(LogMessageFactory.class);

    public static String packageMessage(String message, Object[] args) {
        StringBuilder builder = new StringBuilder(128);
        builder.append(message);
        for (Object arg : args) {
            builder.append("\n").append(arg);
        }
        return builder.toString();
    }

    /**
     * 推送登录日志
     *
     * @param loginLogMessage
     */
    public static void pushLoginLogMessage(LoginLogMessage loginLogMessage) {
        if (StringUtils.isNotBlank(TraceId.logTraceID.get())) {
            loginLogMessage.setTraceId(TraceId.logTraceID.get());
        }
        AbstractClient client = AbstractClient.getClient();
        if (client != null) {
            client.pushSimpleMessage(RivamedLogQueueEnum.RIVAMED_LOGIN_LOG_QUEUE, loginLogMessage);
        }
    }

    /**
     * 推送RabbitMQ日志
     *
     * @param rabbitLogMessage
     */
    public static void pushRabbitLogMessage(RabbitLogMessage rabbitLogMessage) {
        log.info(LogMessageConstant.LOG_TYPE_RABBITMQ_LOG + JsonUtil.toJSONString(rabbitLogMessage));
    }

    /**
     * 推送操作记录日志
     *
     * @param logRecordMessage
     */
    public static void pushLogRecordMessage(LogRecordMessage logRecordMessage) {
        log.info(LogMessageConstant.LOG_TYPE_RECORD_LOG + JsonUtil.toJSONString(logRecordMessage));
    }

}
