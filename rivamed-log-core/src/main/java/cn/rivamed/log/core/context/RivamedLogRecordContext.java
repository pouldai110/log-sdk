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
public class RivamedLogRecordContext {

    private static String sysName = "rivamed-log";

    private static String env = "dev";

    public static final TransmittableThreadLocal<RivamedLogRecordLabel> rivamedLogLabelTTL = new TransmittableThreadLocal<>();


    public static LogRecordMessage buildLogMessage(LogRecordMessage logRecordMessage) {
        RivamedLogRecordLabel rivamedLogRecordLabel = rivamedLogLabelTTL.get();
        logRecordMessage.setUserId(rivamedLogRecordLabel.getUserId());
        logRecordMessage.setUserName(rivamedLogRecordLabel.getUserName());
        logRecordMessage.setDeviceId(rivamedLogRecordLabel.getDeviceId());
        logRecordMessage.setSn(rivamedLogRecordLabel.getSn());
        logRecordMessage.setBizId(rivamedLogRecordLabel.getBizId());
        logRecordMessage.setBizProd(rivamedLogRecordLabel.getBizProd());
        logRecordMessage.setBizAction(rivamedLogRecordLabel.getBizAction());
        logRecordMessage.setLogRecord(rivamedLogRecordLabel.getLogRecord());
        logRecordMessage.setTokenId(rivamedLogRecordLabel.getTokenId());
        logRecordMessage.setTenantId(rivamedLogRecordLabel.getTenantId());
        return logRecordMessage;
    }

    public static String getSysName() {
        return sysName;
    }

    public static void setSysName(String sysName) {
        RivamedLogRecordContext.sysName = sysName;
    }

    public static String getEnv() {
        return env;
    }

    public static void setEnv(String env) {
        RivamedLogRecordContext.env = env;
    }
}
