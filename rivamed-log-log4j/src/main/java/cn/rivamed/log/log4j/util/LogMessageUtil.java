package cn.rivamed.log.log4j.util;

import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.context.RivamedLogRecordContext;
import cn.rivamed.log.core.entity.BaseLogMessage;
import cn.rivamed.log.core.enums.LogTypeEnum;
import cn.rivamed.log.core.util.IpGetter;
import cn.rivamed.log.core.util.LogExceptionStackTrace;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import static cn.rivamed.log.core.entity.TraceId.logSpanID;
import static cn.rivamed.log.core.entity.TraceId.logTraceID;

/**
 * className：LogMessageUtil
 * description：组装日志对象
 *
 * @author Zuo Yang
 * @version 1.0.0
 */
public class LogMessageUtil {

    /**
     * 序列生成器：当日志在一毫秒内打印多次时，发送到服务端排序时无法按照正常顺序显示，因此加一个序列保证同一毫秒内的日志按顺序显示
     * 使用AtomicLong不要使用LongAdder，LongAdder在该场景高并发下无法严格保证顺序性，也不需要考虑Long是否够用，假设每秒打印10万日志，也需要两百多万年才能用的完
     */
    private static final AtomicLong SEQ_BUILDER = new AtomicLong(1);

    public static BaseLogMessage getLogMessage(LoggingEvent loggingEvent) {
        BaseLogMessage logMessage = convertMessage(loggingEvent);
        logMessage.setClassName(loggingEvent.getLoggerName())
                .setThreadName(loggingEvent.getThreadName())
                .setSeq(SEQ_BUILDER.getAndIncrement())
                // dateTime字段用来保存当前服务器的时间戳字符串
                .setBizIP(IpGetter.CURRENT_IP)
                .setBizTime(new Date())
                .setLevel(loggingEvent.getLevel().toString())
                .setSysName(RivamedLogRecordContext.getSysName())
                .setEnv(RivamedLogRecordContext.getEnv())
                .setTraceId(logTraceID.get())
                .setSpanId(logSpanID.get());
        LocationInfo locationInfo = loggingEvent.getLocationInformation();
        String method = locationInfo.getMethodName();
        String line = locationInfo.getLineNumber();
        logMessage.setMethod(method + "(" + locationInfo.getFileName() + ":" + line + ")");
        return logMessage;
    }

    private static BaseLogMessage convertMessage(LoggingEvent loggingEvent) {
        BaseLogMessage logMessage = LogTypeEnum.convertMessageType(loggingEvent.getRenderedMessage());
        if (loggingEvent.getLevel().toInt() == Priority.ERROR_INT) {
            String formatMessage;
            String msg = "";
            if (loggingEvent.getThrowableInformation() != null) {
                msg = LogExceptionStackTrace.errorStackTrace(
                        loggingEvent.getThrowableInformation().getThrowable()).toString();
            }
            if (loggingEvent.getRenderedMessage() != null && loggingEvent.getRenderedMessage()
                    .contains(LogMessageConstant.DELIM_STR)) {
                FormattingTuple format = MessageFormatter.format(loggingEvent.getRenderedMessage(), msg);
                formatMessage = format.getMessage();
            } else {
                formatMessage = loggingEvent.getRenderedMessage() + "\n" + msg;
            }
            logMessage.setStackTrace(formatMessage);
        }
        return logMessage;
    }
}
