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

    public static final TransmittableThreadLocal<RivamedLogRecordLabel> rivamedLogLabelTTL = new TransmittableThreadLocal<>();


    public static LogRecordMessage buildLogMessage(LogRecordMessage logRecordMessage) {
        RivamedLogRecordLabel rivamedLogRecordLabel = rivamedLogLabelTTL.get();
        if (rivamedLogRecordLabel != null) {
            logRecordMessage.setUserId(rivamedLogRecordLabel.getUserId());
            logRecordMessage.setUserName(rivamedLogRecordLabel.getUserName());
            logRecordMessage.setDeviceId(rivamedLogRecordLabel.getDeviceId());
            logRecordMessage.setSn(rivamedLogRecordLabel.getSn());
            logRecordMessage.setTokenId(rivamedLogRecordLabel.getTokenId());
            logRecordMessage.setTenantId(rivamedLogRecordLabel.getTenantId());
        }

        return logRecordMessage;
    }

    public static String getSysName() {
        return sysName;
    }

    public static void setSysName(String sysName) {
        RivamedLogRecordContext.sysName = sysName;
    }

}
