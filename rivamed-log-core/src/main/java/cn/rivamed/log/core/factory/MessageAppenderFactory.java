package cn.rivamed.log.core.factory;

import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.context.RivamedLogRecordContext;
import cn.rivamed.log.core.disruptor.LogMessageProducer;
import cn.rivamed.log.core.disruptor.LogMessageRingBuffer;
import cn.rivamed.log.core.entity.BaseLogMessage;
import cn.rivamed.log.core.entity.RabbitLogMessage;
import cn.rivamed.log.core.entity.TraceId;
import cn.rivamed.log.core.util.IpGetter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.logging.LogLevel;

import java.lang.reflect.Method;
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
    private static final AtomicLong SEQ_BUILDER = new AtomicLong();


    public static void push(BaseLogMessage baseLogMessage) {
        LogMessageProducer producer = new LogMessageProducer(LogMessageRingBuffer.ringBuffer);
        producer.send(baseLogMessage);
    }

    public static void pushRabbitLogMessage(RabbitLogMessage rabbitLogMessage, ProceedingJoinPoint joinPoint) {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method m = ms.getMethod();
        rabbitLogMessage.setTraceId(TraceId.logTraceID.get())
                .setSpanId(TraceId.logSpanID.get())
                .setSysName(RivamedLogRecordContext.getSysName())
                .setEnv(RivamedLogRecordContext.getEnv())
                .setMethod(joinPoint.getSignature().getDeclaringType().getSimpleName() + "." + m.getName())
                .setClassName(ms.getMethod().getDeclaringClass().getName())
                .setThreadName(Thread.currentThread().getName())
                .setBizDetail((String) rabbitLogMessage.getMessage())
                .setBizIP(IpGetter.CURRENT_IP)
                .setLevel(LogLevel.INFO.name())
                .setLogType(LogMessageConstant.LOG_TYPE_RABBITMQ)
                .setSeq(SEQ_BUILDER.getAndIncrement());
        push(rabbitLogMessage);
    }

}
