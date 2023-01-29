package cn.rivamed.log.core.context;


import cn.rivamed.log.core.entity.LogRecordMessage;
import cn.rivamed.log.core.entity.RivamedLogRecordLabel;
import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * Rivamed Log上下文
 *
 * @author Zuo Yang
 * @since 1.0.0
 */
public class RivamedLogContext {

    private static String sysName = "rivamed-log";

    /**
     * 是否收集日志 全局控制
     */
    private static boolean logEnabled = true;

    /**
     * 是否收集SQL日志
     */
    private static boolean sqlEnabled = false;

    /**
     * 是否收集RabbitMQ日志
     */
    private static boolean rabbitmqEnabled = false;

    /**
     * 是否收集定时任务日志
     */
    private static boolean taskEnabled = true;

    /**
     * 是否收集接口请求参数
     */
    private static boolean requestEnabled = true;

    /**
     * 是否收集接口响应参数
     */
    private static boolean responseEnabled = false;

    public static final TransmittableThreadLocal<RivamedLogRecordLabel> rivamedLogLabelTTL = new TransmittableThreadLocal<>();


    public static LogRecordMessage buildLogMessage(LogRecordMessage logRecordMessage) {
        RivamedLogRecordLabel rivamedLogRecordLabel = rivamedLogLabelTTL.get();
        logRecordMessage.setUserId(rivamedLogRecordLabel.getUserId());
        logRecordMessage.setUserName(rivamedLogRecordLabel.getUserName());
        logRecordMessage.setDeviceId(rivamedLogRecordLabel.getDeviceId());
        logRecordMessage.setSn(rivamedLogRecordLabel.getSn());
        logRecordMessage.setTokenId(rivamedLogRecordLabel.getTokenId());
        logRecordMessage.setTenantId(rivamedLogRecordLabel.getTenantId());
        return logRecordMessage;
    }

    public static String getCurrentUser() {
        return rivamedLogLabelTTL.get().getUserName();
    }

    public static String getSysName() {
        return sysName;
    }

    public static void setSysName(String sysName) {
        RivamedLogContext.sysName = sysName;
    }

    public static boolean isLogEnabled() {
        return logEnabled;
    }

    public static void setLogEnabled(boolean logEnabled) {
        RivamedLogContext.logEnabled = logEnabled;
    }

    public static boolean isSqlEnabled() {
        return sqlEnabled;
    }

    public static void setSqlEnabled(boolean sqlEnabled) {
        RivamedLogContext.sqlEnabled = sqlEnabled;
    }

    public static boolean isRabbitmqEnabled() {
        return rabbitmqEnabled;
    }

    public static void setRabbitmqEnabled(boolean rabbitmqEnabled) {
        RivamedLogContext.rabbitmqEnabled = rabbitmqEnabled;
    }

    public static boolean isTaskEnabled() {
        return taskEnabled;
    }

    public static void setTaskEnabled(boolean taskEnabled) {
        RivamedLogContext.taskEnabled = taskEnabled;
    }

    public static boolean isRequestEnabled() {
        return requestEnabled;
    }

    public static void setRequestEnabled(boolean requestEnabled) {
        RivamedLogContext.requestEnabled = requestEnabled;
    }

    public static boolean isResponseEnabled() {
        return responseEnabled;
    }

    public static void setResponseEnabled(boolean responseEnabled) {
        RivamedLogContext.responseEnabled = responseEnabled;
    }
}
