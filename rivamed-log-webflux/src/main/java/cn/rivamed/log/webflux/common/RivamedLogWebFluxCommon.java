package cn.rivamed.log.webflux.common;

import cn.rivamed.log.core.constant.RivamedLogRPCConstant;
import cn.rivamed.log.core.entity.RivamedLogRPCLabel;
import cn.rivamed.log.core.rpc.RivamedLogRPCHandler;
import cn.rivamed.log.core.util.IpGetter;
import cn.rivamed.log.core.util.SpanIdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author Zuo Yang
 * @since 1.3.0
 */
public class RivamedLogWebFluxCommon extends RivamedLogRPCHandler {

    private final static Logger log = LoggerFactory.getLogger(RivamedLogWebFluxCommon.class);

    private static volatile RivamedLogWebFluxCommon rivamedLogWebFluxCommon;

    private static final Integer FIRST = 0;

    public static RivamedLogWebFluxCommon loadInstance() {
        if (rivamedLogWebFluxCommon == null) {
            synchronized (RivamedLogWebFluxCommon.class) {
                if (rivamedLogWebFluxCommon == null) {
                    rivamedLogWebFluxCommon = new RivamedLogWebFluxCommon();
                }
            }
        }
        return rivamedLogWebFluxCommon;
    }

    public ServerWebExchange preHandle(ServerWebExchange exchange, String appName) {
        String traceId = null;
        String spanId = null;
        String preIvkApp = null;
        String preIvkHost = null;
        String preIp = null;
        HttpHeaders headers = exchange.getRequest().getHeaders();
        List<String> traceIds = headers.get(RivamedLogRPCConstant.RIVAMED_LOG_TRACE_KEY);
        if (traceIds != null && traceIds.size() > 0) {
            traceId = traceIds.get(FIRST);
        }
        List<String> spanIds = headers.get(RivamedLogRPCConstant.RIVAMED_LOG_SPANID_KEY);
        if (spanIds != null && spanIds.size() > 0) {
            spanId = spanIds.get(FIRST);
        }
        List<String> preIvkApps = headers.get(RivamedLogRPCConstant.PRE_IVK_APP_KEY);
        if (preIvkApps != null && preIvkApps.size() > 0) {
            preIvkApp = preIvkApps.get(FIRST);
        }
        List<String> preIvkHosts = headers.get(RivamedLogRPCConstant.PRE_IVK_APP_HOST);
        if (preIvkHosts != null && preIvkHosts.size() > 0) {
            preIvkHost = preIvkHosts.get(FIRST);
        }
        List<String> preIps = headers.get(RivamedLogRPCConstant.PRE_IP_KEY);
        if (preIps != null && preIps.size() > 0) {
            preIp = preIps.get(FIRST);
        }

        RivamedLogRPCLabel labelBean = new RivamedLogRPCLabel(preIvkApp, preIvkHost, preIp, traceId, spanId);
        labelBean.putExtData(RivamedLogRPCConstant.WEBFLUX_EXCHANGE, exchange);

        processProviderSide(labelBean);

        if(StringUtils.isNotBlank(labelBean.getTraceId())){
            Consumer<HttpHeaders> httpHeaders = httpHeader -> {
                httpHeader.set(RivamedLogRPCConstant.RIVAMED_LOG_TRACE_KEY, labelBean.getTraceId());
                httpHeader.set(RivamedLogRPCConstant.RIVAMED_LOG_SPANID_KEY, SpanIdGenerator.generateNextSpanId());
                httpHeader.set(RivamedLogRPCConstant.PRE_IVK_APP_KEY, appName);
                httpHeader.set(RivamedLogRPCConstant.PRE_IVK_APP_HOST, IpGetter.getLocalHostName());
                httpHeader.set(RivamedLogRPCConstant.PRE_IP_KEY, IpGetter.getLocalIP());
            };
            ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate().headers(httpHeaders).build();
            return exchange.mutate().request(serverHttpRequest).build();
        }else{
            log.debug("[RIVAMED LOG]本地threadLocal变量没有正确传递traceId,本次调用不传递traceId");
            return exchange;
        }
    }

}
