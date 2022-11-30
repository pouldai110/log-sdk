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

    private static String env = "dev";

    /**
     * 是否收集SQL日志
     */
    private static boolean sqlEnable = true;

    /**
     * 是否收集RabbitMQ日志
     */
    private static boolean rabbitmqEnable = true;

    /**
     * 是否收集定时任务日志
     */
    private static boolean taskEnable = true;

    /**
     * 是否收集接口请求参数
     */
    private static boolean requestEnable = true;

    /**
     * 是否收集接口响应参数
     */
    private static boolean responseEnable = true;

    public static final TransmittableThreadLocal<RivamedLogRecordLabel> rivamedLogLabelTTL = new TransmittableThreadLocal<>();


    public static LogRecordMessage buildLogMessage(LogRecordMessage logRecordMessage) {
        RivamedLogRecordLabel rivamedLogRecordLabel = rivamedLogLabelTTL.get();
        logRecordMessage.setUserId(rivamedLogRecordLabel.getUserId())
                .setUserName(rivamedLogRecordLabel.getUserName())
                .setDeviceId(rivamedLogRecordLabel.getDeviceId())
                .setSn(rivamedLogRecordLabel.getSn())
                .setBizId(rivamedLogRecordLabel.getBizId())
                .setBizProd(rivamedLogRecordLabel.getBizProd())
                .setBizAction(rivamedLogRecordLabel.getBizAction())
                .setLogRecord(rivamedLogRecordLabel.getLogRecord())
                .setTokenId(rivamedLogRecordLabel.getTokenId())
                .setTenantId(rivamedLogRecordLabel.getTenantId());
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

    public static String getEnv() {
        return env;
    }

    public static void setEnv(String env) {
        RivamedLogContext.env = env;
    }

    public static boolean isSqlEnable() {
        return sqlEnable;
    }

    public static void setSqlEnable(boolean sqlEnable) {
        RivamedLogContext.sqlEnable = sqlEnable;
    }

    public static boolean isRabbitmqEnable() {
        return rabbitmqEnable;
    }

    public static void setRabbitmqEnable(boolean rabbitmqEnable) {
        RivamedLogContext.rabbitmqEnable = rabbitmqEnable;
    }

    public static boolean isTaskEnable() {
        return taskEnable;
    }

    public static void setTaskEnable(boolean taskEnable) {
        RivamedLogContext.taskEnable = taskEnable;
    }

    public static boolean isRequestEnable() {
        return requestEnable;
    }

    public static void setRequestEnable(boolean requestEnable) {
        RivamedLogContext.requestEnable = requestEnable;
    }

    public static boolean isResponseEnable() {
        return responseEnable;
    }

    public static void setResponseEnable(boolean responseEnable) {
        RivamedLogContext.responseEnable = responseEnable;
    }
}
