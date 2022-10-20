package cn.rivamed.log.webflux.filter;

import cn.rivamed.log.webflux.common.RivamedLogWebFluxCommon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * webflux 的全局拦截器
 *
 * @author naah
 * @since 1.3.0
 */
public class RivamedLogWebFluxFilter implements WebFilter, Ordered {

    @Value("${spring.application.name}")
    private String appName;

    private static final Logger log = LoggerFactory.getLogger(RivamedLogWebFluxFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(RivamedLogWebFluxCommon.loadInstance().preHandle(exchange, appName))
                .doFinally(signalType -> RivamedLogWebFluxCommon.loadInstance().cleanThreadLocal());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
