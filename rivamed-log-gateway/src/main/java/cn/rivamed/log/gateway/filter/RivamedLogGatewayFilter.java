package cn.rivamed.log.gateway.filter;

import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.entity.TraceId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * gateway 的全局拦截器
 *
 * @author Zuo Yang
 * @since 1.0
 */
public class RivamedLogGatewayFilter implements GlobalFilter, Ordered {

    @Value("${spring.application.name}")
    private String appName;

    private static final Logger log = LoggerFactory.getLogger(RivamedLogGatewayFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, GatewayFilterChain gatewayFilterChain) {

        List<String> traceIds = serverWebExchange.getRequest().getHeaders().get(LogMessageConstant.TRACE_ID);
        if (!CollectionUtils.isEmpty(traceIds)) {
            TraceId.logTraceID.set(traceIds.get(0));
        } else {
            TraceId.set();
        }

        return gatewayFilterChain.filter(serverWebExchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
