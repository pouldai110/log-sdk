package cn.rivamed.log.logback.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.context.RivamedLogContext;
import cn.rivamed.log.core.entity.BaseLogMessage;
import cn.rivamed.log.core.enums.LogTypeEnum;
import cn.rivamed.log.core.factory.LogMessageFactory;
import cn.rivamed.log.core.util.IpUtil;
import cn.rivamed.log.core.util.LogExceptionStackTrace;
import org.slf4j.helpers.MessageFormatter;

import static cn.rivamed.log.core.entity.TraceId.logSpanID;
import static cn.rivamed.log.core.entity.TraceId.logTraceID;


/**
 * className：LogMessageUtil
 * description：
 * time：2022-10-09.14:34
 *
 * @author Zuo Yang
 * @version 1.0.0
 */
public class LogMessageUtil {

    private static void isExpandRunLog(ILoggingEvent logEvent) {
        String traceId;
        String spanId;
        if (!logEvent.getMDCPropertyMap().isEmpty()) {
            traceId = logEvent.getMDCPropertyMap().get(LogMessageConstant.TRACE_ID);
            spanId = logEvent.getMDCPropertyMap().get(LogMessageConstant.SPAN_ID);
            if (traceId != null) {
                logTraceID.set(traceId);
            }
            if (spanId != null) {
                logSpanID.set(spanId);
            }
        }
    }

    public static BaseLogMessage getLogMessage(final ILoggingEvent logEvent) {
        isExpandRunLog(logEvent);
        BaseLogMessage logMessage = convertMessage(logEvent);
        logMessage.setClassName(logEvent.getLoggerName());
        logMessage.setThreadName(logEvent.getThreadName());
        logMessage.setBizIP(IpUtil.CURRENT_IP);
        logMessage.setLevel(logEvent.getLevel().toString());
        logMessage.setSubSysName(RivamedLogContext.getSysName());
        logMessage.setEnv(RivamedLogContext.getEnv());
        logMessage.setTraceId(logTraceID.get());
        logMessage.setSpanId(logSpanID.get());
        StackTraceElement[] stackTraceElements = logEvent.getCallerData();
        if (stackTraceElements != null && stackTraceElements.length > 0) {
            StackTraceElement stackTraceElement = stackTraceElements[0];
            String method = stackTraceElement.getMethodName();
            String line = String.valueOf(stackTraceElement.getLineNumber());
            logMessage.setMethod(method + "(" + stackTraceElement.getFileName() + ":" + line + ")");
        } else {
            logMessage.setMethod(logEvent.getThreadName());
        }
        return logMessage;
    }

    private static BaseLogMessage convertMessage(ILoggingEvent logEvent) {
        BaseLogMessage logMessage = LogTypeEnum.convertMessageType(logEvent.getFormattedMessage());
        if (logEvent.getLevel().equals(Level.ERROR)) {
            String formatMessage = logEvent.getFormattedMessage();

            if (logEvent.getThrowableProxy() != null) {
                ThrowableProxy throwableProxy = (ThrowableProxy) logEvent.getThrowableProxy();
                String[] args = new String[]{logEvent.getFormattedMessage() + "\n" + LogExceptionStackTrace.errorStackTrace(throwableProxy.getThrowable()).toString()};
                formatMessage = packageMessage("{}", args);
            } else {
                Object[] args = logEvent.getArgumentArray();
                if (args != null) {
                    for (int i = 0; i < args.length; i++) {
                        if (args[i] instanceof Throwable) {
                            args[i] = LogExceptionStackTrace.errorStackTrace(args[i]);
                        }
                    }
                    formatMessage = packageMessage(logEvent.getMessage(), args);
                }
            }
            logMessage.setStackTrace(formatMessage);
        }
        return logMessage;
    }

    private static String packageMessage(String message, Object[] args) {
        if (message != null && message.contains(LogMessageConstant.DELIM_STR)) {
            return MessageFormatter.arrayFormat(message, args).getMessage();
        }
        return LogMessageFactory.packageMessage(message, args);
    }
}
