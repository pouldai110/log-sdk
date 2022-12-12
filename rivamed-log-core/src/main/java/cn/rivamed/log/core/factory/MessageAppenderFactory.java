package cn.rivamed.log.core.factory;

import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.context.RivamedLogContext;
import cn.rivamed.log.core.disruptor.LogMessageRingBuffer;
import cn.rivamed.log.core.entity.BaseLogMessage;
import cn.rivamed.log.core.entity.RabbitLogMessage;
import cn.rivamed.log.core.entity.TraceId;
import cn.rivamed.log.core.enums.LogTypeEnum;
import cn.rivamed.log.core.util.IpUtil;
import cn.rivamed.log.core.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.logging.LogLevel;

import java.util.concurrent.atomic.AtomicLong;

/**
 * className：MessageAppenderFactory
 * description： 日志推送
 * time：2022-10-03.14:18
 *
 * @author Zuo Yang
 * @version 1.0.0
 */
public class MessageAppenderFactory {

    /**
     * 序列生成器：当日志在一毫秒内打印多次时，发送到服务端排序时无法按照正常顺序显示，因此加一个序列保证同一毫秒内的日志按顺序显示
     * 使用AtomicLong不要使用LongAdder，LongAdder在该场景高并发下无法严格保证顺序性，也不需要考虑Long是否够用，假设每秒打印10万日志，也需要两百多万年才能用的完
     */
    private static final AtomicLong SEQ_BUILDER = new AtomicLong(1);


    public static void push(BaseLogMessage baseLogMessage) {
        if (StringUtils.isNotBlank(baseLogMessage.getTraceId()) && checkSqlLog(baseLogMessage)) {
            baseLogMessage.setSeq(SEQ_BUILDER.getAndIncrement());
            LogMessageRingBuffer.ringBuffer.publishEvent((event, sequence) -> event.setBaseLogMessage(baseLogMessage));
        }
    }

    /**
     * 根据配置判断是否收集SQL日志
     *
     * @param baseLogMessage
     * @return
     */
    private static boolean checkSqlLog(BaseLogMessage baseLogMessage) {
        Boolean flag = true;
        if (!RivamedLogContext.isSqlEnable() && (baseLogMessage.getBizDetail().startsWith(LogMessageConstant.LEFT_EQUALS) || baseLogMessage.getBizDetail().startsWith(LogMessageConstant.RIGHT_EQUALS) || baseLogMessage.getClassName().equals(LogMessageConstant.HIBERNATE_SQL_PATH))) {
            flag = false;
        }
        return flag;
    }

    public static void pushRabbitLogMessage(RabbitLogMessage rabbitLogMessage) {
        // 如果不是String类型，就转成JSON
        String message;
        if (rabbitLogMessage.getMessage() instanceof String) {
            message = (String) rabbitLogMessage.getMessage();
        } else {
            message = JsonUtil.toJSONString(rabbitLogMessage.getMessage());
        }
        rabbitLogMessage.setTraceId(TraceId.logTraceID.get());
        rabbitLogMessage.setSpanId(TraceId.logSpanID.get());
        rabbitLogMessage.setSubSysName(RivamedLogContext.getSysName());
        rabbitLogMessage.setEnv(RivamedLogContext.getEnv());
        rabbitLogMessage.setThreadName(Thread.currentThread().getName());
        rabbitLogMessage.setBizDetail(message);
        rabbitLogMessage.setBizIP(IpUtil.CURRENT_IP);
        rabbitLogMessage.setLevel(LogLevel.INFO.name());
        rabbitLogMessage.setLogType(LogTypeEnum.LOG_TYPE_RABBITMQ_LOG.getType());
        rabbitLogMessage.setSeq(SEQ_BUILDER.getAndIncrement());
        push(rabbitLogMessage);
    }

}
