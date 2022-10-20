package cn.rivamed.log.feign.filter;

import cn.rivamed.log.core.constant.RivamedLogRPCConstant;
import cn.rivamed.log.core.context.RivamedLogRPCContext;
import cn.rivamed.log.core.util.IpGetter;
import cn.rivamed.log.core.util.SpanIdGenerator;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * feign的拦截器
 *
 * @author Zuo Yang
 * @since 1.0.0
 */
public class RivamedLogFeignFilter implements RequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RivamedLogFeignFilter.class);

    @Value("${spring.application.name}")
    private String appName;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String traceId = RivamedLogRPCContext.getTraceId();

        if(StringUtils.isNotBlank(traceId)){

            requestTemplate.header(RivamedLogRPCConstant.RIVAMED_LOG_TRACE_KEY, traceId);
            requestTemplate.header(RivamedLogRPCConstant.RIVAMED_LOG_SPANID_KEY, SpanIdGenerator.generateNextSpanId());
            requestTemplate.header(RivamedLogRPCConstant.PRE_IVK_APP_KEY, appName);
            requestTemplate.header(RivamedLogRPCConstant.PRE_IVK_APP_HOST, IpGetter.getLocalHostName());
            requestTemplate.header(RivamedLogRPCConstant.PRE_IP_KEY, IpGetter.getLocalIP());
        }else{
            log.debug("[RIVAMED LOG]本地threadLocal变量没有正确传递traceId,本次调用不传递traceId");
        }
    }
}
