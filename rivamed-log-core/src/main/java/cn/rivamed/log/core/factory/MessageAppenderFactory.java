package cn.rivamed.log.core.factory;

import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.context.RivamedLogRecordContext;
import cn.rivamed.log.core.disruptor.LogMessageRingBuffer;
import cn.rivamed.log.core.entity.BaseLogMessage;
import cn.rivamed.log.core.entity.RabbitLogMessage;
import cn.rivamed.log.core.entity.TraceId;
import cn.rivamed.log.core.util.IpGetter;
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
        if (StringUtils.isNotBlank(baseLogMessage.getTraceId())) {
            LogMessageRingBuffer.ringBuffer.publishEvent((event, sequence) -> event.setBaseLogMessage(baseLogMessage));
        }
    }

    public static void pushRabbitLogMessage(RabbitLogMessage rabbitLogMessage) {
        // 如果不是String类型，就转成JSON
        String message;
        if (rabbitLogMessage.getMessage() instanceof String) {
            message = (String) rabbitLogMessage.getMessage();
        } else {
            message = JsonUtil.toJSONString(rabbitLogMessage.getMessage());
        }
        rabbitLogMessage.setTraceId(TraceId.logTraceID.get())
                .setSpanId(TraceId.logSpanID.get())
                .setSysName(RivamedLogRecordContext.getSysName())
                .setEnv(RivamedLogRecordContext.getEnv())
                .setThreadName(Thread.currentThread().getName())
                .setBizDetail(message)
                .setBizIP(IpGetter.CURRENT_IP)
                .setLevel(LogLevel.INFO.name())
                .setLogType(LogMessageConstant.LOG_TYPE_RABBITMQ)
                .setSeq(SEQ_BUILDER.getAndIncrement());
        push(rabbitLogMessage);
    }

}
