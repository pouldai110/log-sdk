package cn.rivamed.log.log4j.util;

import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.context.RivamedLogContext;
import cn.rivamed.log.core.entity.BaseLogMessage;
import cn.rivamed.log.core.enums.LogTypeEnum;
import cn.rivamed.log.core.util.IpUtil;
import cn.rivamed.log.core.util.LogExceptionStackTrace;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

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

    private static void isExpandRunLog(LoggingEvent loggingEvent) {
        String traceId = (String) loggingEvent.getMDC(LogMessageConstant.TRACE_ID);
        String spanId = (String) loggingEvent.getMDC(LogMessageConstant.SPAN_ID);
        if (traceId != null) {
            logTraceID.set(traceId);
        }
        if (spanId != null) {
            logSpanID.set(spanId);
        }
    }

    public static BaseLogMessage getLogMessage(LoggingEvent loggingEvent) {
        isExpandRunLog(loggingEvent);
        BaseLogMessage logMessage = convertMessage(loggingEvent);
        logMessage.setClassName(loggingEvent.getLoggerName());
        logMessage.setThreadName(loggingEvent.getThreadName());
        logMessage.setBizIP(IpUtil.CURRENT_IP);
        logMessage.setLevel(loggingEvent.getLevel().toString());
        logMessage.setSubSysName(RivamedLogContext.getSysName());
        logMessage.setTraceId(logTraceID.get());
        logMessage.setSpanId(logSpanID.get());
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
