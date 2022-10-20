package cn.rivamed.log.core.rpc;

import cn.rivamed.log.core.constant.RivamedLogRPCConstant;
import cn.rivamed.log.core.context.AspectLogRPCContext;
import cn.rivamed.log.core.context.RivamedLogRPCContext;
import cn.rivamed.log.core.entity.RivamedLogRPCLabel;
import cn.rivamed.log.core.util.IpGetter;
import cn.rivamed.log.core.util.RivamedLogRPCLabelGenerator;
import cn.rivamed.log.core.util.TraceIdGenerator;
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
public class RivamedLogRPCHandler {

    protected static final Logger log = LoggerFactory.getLogger(RivamedLogRPCHandler.class);

    public void processProviderSide(RivamedLogRPCLabel labelBean) {
        if (StringUtils.isBlank(labelBean.getPreIvkApp())) {
            labelBean.setPreIvkApp(RivamedLogRPCConstant.UNKNOWN);
        }
        RivamedLogRPCContext.putPreIvkApp(labelBean.getPreIvkApp());

        if (StringUtils.isBlank(labelBean.getPreIvkHost())) {
            labelBean.setPreIvkHost(RivamedLogRPCConstant.UNKNOWN);
        }
        RivamedLogRPCContext.putPreIvkHost(labelBean.getPreIvkHost());

        if (StringUtils.isBlank(labelBean.getPreIp())) {
            labelBean.setPreIp(RivamedLogRPCConstant.UNKNOWN);
        }
        RivamedLogRPCContext.putPreIp(labelBean.getPreIp());

        //如果没有获取到，则重新生成一个traceId
        if (StringUtils.isBlank(labelBean.getTraceId())) {
            labelBean.setTraceId(TraceIdGenerator.generate());
            log.debug("[RIVAMED LOG]可能上一个节点[{}]没有正确传递traceId,重新生成traceId[{}]", labelBean.getPreIvkApp(), labelBean.getTraceId());
        }

        //往TLog上下文里放当前获取到的spanId，如果spanId为空，会放入初始值
        RivamedLogRPCContext.putSpanId(labelBean.getSpanId());

        //往TLog上下文里放一个当前的traceId
        RivamedLogRPCContext.putTraceId(labelBean.getTraceId());

        //往TLog上下文里放当前的IP
        RivamedLogRPCContext.putCurrIp(IpGetter.getLocalIP());

        //生成日志标签
        String tlogLabel = RivamedLogRPCLabelGenerator.generateTLogLabel(labelBean.getPreIvkApp(),
                labelBean.getPreIvkHost(),
                labelBean.getPreIp(),
                RivamedLogRPCContext.getCurrIp(),
                labelBean.getTraceId(),
                RivamedLogRPCContext.getSpanId());

        //往日志切面器里放一个日志前缀
        AspectLogRPCContext.putLogValue(tlogLabel);

        //目前无论是不是MDC，都往MDC里放参数，这样就避免了log4j2的特殊设置
        MDC.put(RivamedLogRPCConstant.MDC_KEY, tlogLabel);
        MDC.put(RivamedLogRPCConstant.RIVAMED_LOG_TRACE_KEY, RivamedLogRPCContext.getTraceId());
        MDC.put(RivamedLogRPCConstant.RIVAMED_LOG_SPANID_KEY, RivamedLogRPCContext.getSpanId());
        MDC.put(RivamedLogRPCConstant.CURR_IP_KEY, RivamedLogRPCContext.getCurrIp());
        MDC.put(RivamedLogRPCConstant.PRE_IP_KEY, RivamedLogRPCContext.getPreIp());
        MDC.put(RivamedLogRPCConstant.PRE_IVK_APP_HOST, RivamedLogRPCContext.getPreIvkHost());
        MDC.put(RivamedLogRPCConstant.PRE_IVK_APP_KEY, RivamedLogRPCContext.getPreIvkApp());
    }

    public void cleanThreadLocal() {
        //移除ThreadLocal里的数据
        RivamedLogRPCContext.removePreIvkApp();
        RivamedLogRPCContext.removePreIvkHost();
        RivamedLogRPCContext.removePreIp();
        RivamedLogRPCContext.removeCurrIp();
        RivamedLogRPCContext.removeTraceId();
        RivamedLogRPCContext.removeSpanId();
        AspectLogRPCContext.remove();

        //移除MDC里的信息
        MDC.remove(RivamedLogRPCConstant.MDC_KEY);
        MDC.remove(RivamedLogRPCConstant.RIVAMED_LOG_TRACE_KEY);
        MDC.remove(RivamedLogRPCConstant.RIVAMED_LOG_SPANID_KEY);
        MDC.remove(RivamedLogRPCConstant.CURR_IP_KEY);
        MDC.remove(RivamedLogRPCConstant.PRE_IP_KEY);
        MDC.remove(RivamedLogRPCConstant.PRE_IVK_APP_HOST);
        MDC.remove(RivamedLogRPCConstant.PRE_IVK_APP_KEY);
    }
}
