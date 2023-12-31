package cn.rivamed.log.core.rpc;

import cn.rivamed.log.core.constant.LogMessageContextConstant;
import cn.rivamed.log.core.context.RivamedLogContext;
import cn.rivamed.log.core.entity.RivamedLogRecordLabel;
import cn.rivamed.log.core.entity.TraceId;
import com.alibaba.ttl.TransmittableThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Rivamed Log的RPC处理逻辑的封装类
 *
 * @author Zuo Yang
 * @since 1.2.0
 */
public class RivamedLogRecordHandler {

    protected static final Logger log = LoggerFactory.getLogger(RivamedLogRecordHandler.class);

    public void processProviderSide(RivamedLogRecordLabel labelBean) {
        TransmittableThreadLocal<RivamedLogRecordLabel> rivamedLogLabelTTL = RivamedLogContext.rivamedLogLabelTTL;

        rivamedLogLabelTTL.set(labelBean);
        //目前无论是不是MDC，都往MDC里放参数，这样就避免了log4j2的特殊设置,  log4j底层用的 HashTable value不能为空，需要先判空再设置
        if (StringUtils.isNotBlank(labelBean.getUserId())) {
            MDC.put(LogMessageContextConstant.CONTEXT_USERID, labelBean.getUserId());
        }
        if (StringUtils.isNotBlank(labelBean.getUserName())) {
            MDC.put(LogMessageContextConstant.CONTEXT_USERNAME, labelBean.getUserName());
        }
        if (StringUtils.isNotBlank(labelBean.getDeviceId())) {
            MDC.put(LogMessageContextConstant.CONTEXT_DEVICEID, labelBean.getDeviceId());
        }
        if (StringUtils.isNotBlank(labelBean.getSn())) {
            MDC.put(LogMessageContextConstant.CONTEXT_SN, labelBean.getSn());
        }
        if (StringUtils.isNotBlank(labelBean.getTokenId())) {
            MDC.put(LogMessageContextConstant.CONTEXT_TOKENID, labelBean.getTokenId());
        }
        if (StringUtils.isNotBlank(labelBean.getTenantId())) {
            MDC.put(LogMessageContextConstant.CONTEXT_TENANTID, labelBean.getTenantId());
        }
    }

    public void cleanThreadLocal() {
        //移除ThreadLocal里的数据
        RivamedLogContext.rivamedLogLabelTTL.remove();

        //移除MDC里的信息
        MDC.remove(LogMessageContextConstant.CONTEXT_USERID);
        MDC.remove(LogMessageContextConstant.CONTEXT_USERNAME);
        MDC.remove(LogMessageContextConstant.CONTEXT_DEVICEID);
        MDC.remove(LogMessageContextConstant.CONTEXT_SN);
        MDC.remove(LogMessageContextConstant.CONTEXT_TOKENID);
        MDC.remove(LogMessageContextConstant.CONTEXT_TENANTID);

        TraceId.logTraceID.remove();
        TraceId.logSpanID.remove();
    }
}
