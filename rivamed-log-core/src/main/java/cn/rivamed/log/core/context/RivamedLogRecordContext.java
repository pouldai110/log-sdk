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
