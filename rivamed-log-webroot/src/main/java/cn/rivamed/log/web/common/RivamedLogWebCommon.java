package cn.rivamed.log.web.common;

import cn.rivamed.log.core.constant.RivamedLogRPCConstant;
import cn.rivamed.log.core.entity.RivamedLogRPCLabel;
import cn.rivamed.log.core.rpc.RivamedLogRPCHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * RIVAMED LOG web这块的逻辑封装类
 *
 * @author Zuo Yang
 * @since 1.1.5
 */
public class RivamedLogWebCommon extends RivamedLogRPCHandler {

    private final static Logger log = LoggerFactory.getLogger(RivamedLogWebCommon.class);

    private static volatile RivamedLogWebCommon rivamedLogWebCommon;

    public static RivamedLogWebCommon loadInstance() {
        if (rivamedLogWebCommon == null) {
            synchronized (RivamedLogWebCommon.class) {
                if (rivamedLogWebCommon == null) {
                    rivamedLogWebCommon = new RivamedLogWebCommon();
                }
            }
        }
        return rivamedLogWebCommon;
    }

    public void preHandle(HttpServletRequest request) {
        String traceId = request.getHeader(RivamedLogRPCConstant.RIVAMED_LOG_TRACE_KEY);
        String spanId = request.getHeader(RivamedLogRPCConstant.RIVAMED_LOG_SPANID_KEY);
        String preIvkApp = request.getHeader(RivamedLogRPCConstant.PRE_IVK_APP_KEY);
        String preIvkHost = request.getHeader(RivamedLogRPCConstant.PRE_IVK_APP_HOST);
        String preIp = request.getHeader(RivamedLogRPCConstant.PRE_IP_KEY);

        RivamedLogRPCLabel labelBean = new RivamedLogRPCLabel(preIvkApp, preIvkHost, preIp, traceId, spanId);

        processProviderSide(labelBean);
    }

    public void afterCompletion() {
        cleanThreadLocal();
    }
}
