package cn.rivamed.log.web.interceptor;

import cn.rivamed.log.core.constant.RivamedLogRPCConstant;
import cn.rivamed.log.core.context.RivamedLogRPCContext;
import cn.rivamed.log.web.common.RivamedLogWebCommon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * web controller的拦截器
 *
 * @author Bryan.Zhang
 * @since 1.1.5
 */
public class RivamedLogWebInterceptor extends AbstractRivamedLogWebHandlerMethodInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RivamedLogWebInterceptor.class);

    @Override
    public boolean preHandleByHandlerMethod(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        RivamedLogWebCommon.loadInstance().preHandle(request);
        //把traceId放入response的header，为了方便有些人有这样的需求，从前端拿整条链路的traceId
        response.addHeader(RivamedLogRPCConstant.RIVAMED_LOG_TRACE_KEY, RivamedLogRPCContext.getTraceId());
        return true;
    }

    @Override
    public void postHandleByHandlerMethod(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletionByHandlerMethod(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RivamedLogWebCommon.loadInstance().afterCompletion();
    }
}
