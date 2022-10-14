package cn.rivamed.log.logback.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.entity.BaseLogMessage;
import cn.rivamed.log.core.factory.LogMessageFactory;
import cn.rivamed.log.core.util.IpGetter;
import cn.rivamed.log.core.util.LogExceptionStackTrace;
import org.slf4j.helpers.MessageFormatter;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

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

    /**
     * 序列生成器：当日志在一毫秒内打印多次时，发送到服务端排序时无法按照正常顺序显示，因此加一个序列保证同一毫秒内的日志按顺序显示
     * 使用AtomicLong不要使用LongAdder，LongAdder在该场景高并发下无法严格保证顺序性，也不需要考虑Long是否够用，假设每秒打印10万日志，也需要两百多万年才能用的完
     */
    private static final AtomicLong SEQ_BUILDER = new AtomicLong();

    private static String isExpandRunLog(ILoggingEvent logEvent) {
        String traceId = null;
        if (!logEvent.getMDCPropertyMap().isEmpty()) {
            traceId = logEvent.getMDCPropertyMap().get(LogMessageConstant.TRACE_ID);
            if (traceId != null) {
                logTraceID.set(traceId);
            }
        }
        return traceId;
    }

    public static BaseLogMessage getLogMessage(final String appName, final String env, final ILoggingEvent logEvent) {
        isExpandRunLog(logEvent);
        String formattedMessage = getMessage(logEvent);

        BaseLogMessage logMessage = new BaseLogMessage();
        logMessage.setClassName(logEvent.getLoggerName());
        logMessage.setThreadName(logEvent.getThreadName());
        logMessage.setSeq(SEQ_BUILDER.getAndIncrement());
        StackTraceElement[] stackTraceElements = logEvent.getCallerData();
        if (stackTraceElements != null && stackTraceElements.length > 0) {
            StackTraceElement stackTraceElement = stackTraceElements[0];
            String method = stackTraceElement.getMethodName();
            String line = String.valueOf(stackTraceElement.getLineNumber());
            logMessage.setMethod(method + "(" + stackTraceElement.getFileName() + ":" + line + ")");
        } else {
            logMessage.setMethod(logEvent.getThreadName());
        }
        // dateTime字段用来保存当前服务器的时间戳字符串
        logMessage.setBizIP(IpGetter.CURRENT_IP);
        logMessage.setBizTime(new Date());
        logMessage.setLevel(logEvent.getLevel().toString());
        logMessage.setSysName(appName);
        logMessage.setEnv(env);
        logMessage.setBizDetail(formattedMessage);
        logMessage.setStackTrace(formattedMessage);
        logMessage.setTraceId(logTraceID.get());

        return logMessage;
    }

    private static String getMessage(ILoggingEvent logEvent) {
        if (logEvent.getLevel().equals(Level.ERROR)) {
            if (logEvent.getThrowableProxy() != null) {
                ThrowableProxy throwableProxy = (ThrowableProxy) logEvent.getThrowableProxy();
                String[] args = new String[]{logEvent.getFormattedMessage() + "\n" + LogExceptionStackTrace.errorStackTrace(throwableProxy.getThrowable()).toString()};
                return packageMessage("{}", args);
            } else {
                Object[] args = logEvent.getArgumentArray();
                if (args != null) {
                    for (int i = 0; i < args.length; i++) {
                        if (args[i] instanceof Throwable) {
                            args[i] = LogExceptionStackTrace.errorStackTrace(args[i]);
                        }
                    }
                    return packageMessage(logEvent.getMessage(), args);
                }
            }
        }
        return logEvent.getFormattedMessage();
    }

    private static String packageMessage(String message, Object[] args) {
        if (message != null && message.contains(LogMessageConstant.DELIM_STR)) {
            return MessageFormatter.arrayFormat(message, args).getMessage();
        }
        return LogMessageFactory.packageMessage(message, args);
    }
}
