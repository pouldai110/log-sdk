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

    private static final TransmittableThreadLocal<String> sysNameTTL = new TransmittableThreadLocal<>();

    private static final TransmittableThreadLocal<String> envTTL = new TransmittableThreadLocal<>();

    public static final TransmittableThreadLocal<RivamedLogRecordLabel> rivamedLogLabelTTL = new TransmittableThreadLocal<>();

    public static void putSysName(String sysName) {
        sysNameTTL.set(sysName);
    }

    public static String getSysName() {
        return sysNameTTL.get();
    }

    public static void removeSysName() {
        sysNameTTL.remove();
    }


    public static void putEnv(String env) {
        envTTL.set(env);
    }

    public static String getEnv() {
        return envTTL.get();
    }

    public static void removeEnv() {
        envTTL.remove();
    }

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
}
