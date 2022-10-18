package cn.rivamed.log.log4j.context;


import cn.rivamed.log.core.entity.LogRecordMessage;
import cn.rivamed.log.core.entity.RivamedLogLabel;
import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * Rivamed Log Properties上下文
 *
 * @author Zuo Yang
 * @since 1.0.0
 */
public class RivamedLogPropertiesContext {

    private static final TransmittableThreadLocal<String> sysNameTTL = new TransmittableThreadLocal<>();

    private static final TransmittableThreadLocal<String> envTTL = new TransmittableThreadLocal<>();

    public static final TransmittableThreadLocal<RivamedLogLabel> rivamedLogLabelTTL = new TransmittableThreadLocal<>();

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
        RivamedLogLabel rivamedLogLabel = rivamedLogLabelTTL.get();
        logRecordMessage.setUserId(rivamedLogLabel.getUserId());
        logRecordMessage.setUserName(rivamedLogLabel.getUserName());
        logRecordMessage.setDeviceId(rivamedLogLabel.getDeviceId());
        logRecordMessage.setSn(rivamedLogLabel.getSn());
        logRecordMessage.setBizId(rivamedLogLabel.getBizId());
        logRecordMessage.setBizProd(rivamedLogLabel.getBizProd());
        logRecordMessage.setBizAction(rivamedLogLabel.getBizAction());
        logRecordMessage.setLogRecord(rivamedLogLabel.getLogRecord());
        logRecordMessage.setTokenId(rivamedLogLabel.getTokenId());
        logRecordMessage.setTenantId(rivamedLogLabel.getTenantId());
        return logRecordMessage;
    }
}
