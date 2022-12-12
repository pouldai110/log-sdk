package cn.rivamed.log.log4j2.util;

import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.context.RivamedLogContext;
import cn.rivamed.log.core.entity.BaseLogMessage;
import cn.rivamed.log.core.enums.LogTypeEnum;
import cn.rivamed.log.core.factory.LogMessageFactory;
import cn.rivamed.log.core.util.IpUtil;
import cn.rivamed.log.core.util.LogExceptionStackTrace;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;

import static cn.rivamed.log.core.entity.TraceId.logSpanID;
import static cn.rivamed.log.core.entity.TraceId.logTraceID;
import static org.apache.logging.log4j.message.ParameterizedMessageFactory.INSTANCE;

/**
 * className：LogMessageUtil
 * description：
 * time：2022-10-09.14:34
 *
 * @author Zuo Yang
 * @version 1.0.0
 */
public class LogMessageUtil {

    private static void isExpandRunLog(LogEvent logEvent) {
        String traceId;
        String spanId;
        if (!logEvent.getContextData().isEmpty()) {
            traceId = logEvent.getContextData().toMap().get(LogMessageConstant.TRACE_ID);
            if (traceId != null) {
                logTraceID.set(traceId);
            }
            spanId = logEvent.getContextData().toMap().get(LogMessageConstant.SPAN_ID);
            if (traceId != null) {
                logSpanID.set(spanId);
            }
        }
    }

    public static BaseLogMessage getLogMessage(LogEvent logEvent) {
        isExpandRunLog(logEvent);
        BaseLogMessage logMessage = convertMessage(logEvent);
        logMessage.setClassName(logEvent.getLoggerName());
        logMessage.setThreadName(logEvent.getThreadName());
        logMessage.setBizIP(IpUtil.CURRENT_IP);
        logMessage.setLevel(logEvent.getLevel().toString());
        logMessage.setSubSysName(RivamedLogContext.getSysName());
        logMessage.setTraceId(logTraceID.get());
        logMessage.setSpanId(logSpanID.get());
        StackTraceElement stackTraceElement = logEvent.getSource();
        if (stackTraceElement != null) {
            String method = stackTraceElement.getMethodName();
            String line = String.valueOf(stackTraceElement.getLineNumber());
            logMessage.setMethod(method + "(" + stackTraceElement.getFileName() + ":" + line + ")");
        } else {
            logMessage.setMethod(logEvent.getThreadName());
        }
        return logMessage;
    }

    private static BaseLogMessage convertMessage(LogEvent logEvent) {
        BaseLogMessage logMessage = LogTypeEnum.convertMessageType(logEvent.getMessage().getFormattedMessage());
        if (logEvent.getLevel().equals(Level.ERROR)) {
            // 如果占位符个数与参数个数相同,即使最后一个参数为Throwable类型,logEvent.getThrown()也会为null
            Throwable thrown = logEvent.getThrown();
            String formatMessage = logEvent.getMessage().getFormat();
            Object[] args = logEvent.getMessage().getParameters();
            if (args != null) {
                for (int i = 0, l = args.length; i < l; i++) {
                    // 当最后一个参数与thrown是同一个对象时,表示logEvent.getThrown()不为null,并且占位符个数与参数个数不相同,则将该thrown留后处理
                    if ((i != l - 1 || args[i] != thrown) && args[i] instanceof Throwable) {
                        args[i] = LogExceptionStackTrace.errorStackTrace(args[i]);
                    }
                }
                formatMessage = packageMessage(formatMessage, args);
            }
            if (thrown != null) {
                formatMessage = packageMessage(formatMessage,
                        new String[]{LogExceptionStackTrace.errorStackTrace(thrown).toString()});
            }
            logMessage.setStackTrace(formatMessage);
        }
        return logMessage;
    }

    private static String packageMessage(String message, Object[] args) {
        if (message != null && message.contains(LogMessageConstant.DELIM_STR)) {
            return INSTANCE.newMessage(message, args).getFormattedMessage();
        }
        return LogMessageFactory.packageMessage(message, args);
    }
}
