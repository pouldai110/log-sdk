package cn.rivamed.log.log4j2.util;

import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.entity.BaseLogMessage;
import cn.rivamed.log.core.factory.LogMessageFactory;
import cn.rivamed.log.core.util.IpGetter;
import cn.rivamed.log.core.util.LogExceptionStackTrace;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

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

    /**
     * 序列生成器：当日志在一毫秒内打印多次时，发送到服务端排序时无法按照正常顺序显示，因此加一个序列保证同一毫秒内的日志按顺序显示
     * 使用AtomicLong不要使用LongAdder，LongAdder在该场景高并发下无法严格保证顺序性，也不需要考虑Long是否够用，假设每秒打印10万日志，也需要两百多万年才能用的完
     */
    private static final AtomicLong SEQ_BUILDER = new AtomicLong();

    private static String isExpandRunLog(LogEvent logEvent) {
        String traceId = null;
        if (!logEvent.getContextData().isEmpty()) {
            traceId = logEvent.getContextData().toMap().get(LogMessageConstant.TRACE_ID);
            if (traceId != null) {
                logTraceID.set(traceId);
            }
        }
        return traceId;
    }

    public static BaseLogMessage getLogMessage(String appName, String env, LogEvent logEvent) {
        isExpandRunLog(logEvent);
        String formattedMessage = getMessage(logEvent);

        BaseLogMessage logMessage = new BaseLogMessage();
        logMessage.setClassName(logEvent.getLoggerName());
        logMessage.setThreadName(logEvent.getThreadName());
        logMessage.setSeq(SEQ_BUILDER.getAndIncrement());
        StackTraceElement stackTraceElement = logEvent.getSource();
        if (stackTraceElement != null) {
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

    private static String getMessage(LogEvent logEvent) {
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
                return packageMessage(formatMessage,
                        new String[]{LogExceptionStackTrace.errorStackTrace(thrown).toString()});
            }
            return formatMessage;
        }
        return logEvent.getMessage().getFormattedMessage();
    }

    private static String packageMessage(String message, Object[] args) {
        if (message != null && message.contains(LogMessageConstant.DELIM_STR)) {
            return INSTANCE.newMessage(message, args).getFormattedMessage();
        }
        return LogMessageFactory.packageMessage(message, args);
    }
}
