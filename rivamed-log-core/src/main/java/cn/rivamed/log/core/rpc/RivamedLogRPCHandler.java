package cn.rivamed.log.core.rpc;

import cn.rivamed.log.core.constant.LogMessageContextConstant;
import cn.rivamed.log.core.context.RivamedLogContext;
import cn.rivamed.log.core.context.RivamedLogLabel;
import com.alibaba.ttl.TransmittableThreadLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * TLog的RPC处理逻辑的封装类
 *
 * @author Zuo Yang
 * @since 1.2.0
 */
public class RivamedLogRPCHandler {

    protected static final Logger log = LoggerFactory.getLogger(RivamedLogRPCHandler.class);

    public void processProviderSide(RivamedLogLabel labelBean) {
        TransmittableThreadLocal<RivamedLogLabel> rivamedLogLabelTTL = RivamedLogContext.rivamedLogLabelTTL;

        rivamedLogLabelTTL.set(labelBean);

        //目前无论是不是MDC，都往MDC里放参数，这样就避免了log4j2的特殊设置
        MDC.put(LogMessageContextConstant.CONTEXT_USERID, labelBean.getUserId());
        MDC.put(LogMessageContextConstant.CONTEXT_USERNAME, labelBean.getUserName());
        MDC.put(LogMessageContextConstant.CONTEXT_DEVICEID, labelBean.getDeviceId());
        MDC.put(LogMessageContextConstant.CONTEXT_SN, labelBean.getSn());
        MDC.put(LogMessageContextConstant.CONTEXT_BIZID, labelBean.getBizId());
        MDC.put(LogMessageContextConstant.CONTEXT_BIZPROD, labelBean.getBizProd());
        MDC.put(LogMessageContextConstant.CONTEXT_BIZACTION, labelBean.getBizAction());
        MDC.put(LogMessageContextConstant.CONTEXT_LOGRECORD, labelBean.getLogRecord());
        MDC.put(LogMessageContextConstant.CONTEXT_TOKENID, labelBean.getTokenId());
        MDC.put(LogMessageContextConstant.CONTEXT_TENANTID, labelBean.getTenantId());
    }

    public void cleanThreadLocal() {
        //移除ThreadLocal里的数据
        RivamedLogContext.rivamedLogLabelTTL.remove();

        //移除MDC里的信息
        MDC.remove(LogMessageContextConstant.CONTEXT_USERID);
        MDC.remove(LogMessageContextConstant.CONTEXT_USERNAME);
        MDC.remove(LogMessageContextConstant.CONTEXT_DEVICEID);
        MDC.remove(LogMessageContextConstant.CONTEXT_SN);
        MDC.remove(LogMessageContextConstant.CONTEXT_BIZID);
        MDC.remove(LogMessageContextConstant.CONTEXT_BIZPROD);
        MDC.remove(LogMessageContextConstant.CONTEXT_BIZACTION);
        MDC.remove(LogMessageContextConstant.CONTEXT_LOGRECORD);
        MDC.remove(LogMessageContextConstant.CONTEXT_TOKENID);
        MDC.remove(LogMessageContextConstant.CONTEXT_TENANTID);
    }
}
