package cn.rivamed.log.gateway.filter;

import cn.rivamed.log.webflux.common.RivamedLogWebFluxCommon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

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
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(RivamedLogWebFluxCommon.loadInstance().preHandle(exchange, appName))
                .doFinally(signalType -> RivamedLogWebFluxCommon.loadInstance().cleanThreadLocal());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
