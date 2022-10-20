package cn.rivamed.log.web.filter;

import cn.rivamed.log.web.common.RivamedLogWebCommon;
import cn.rivamed.log.core.constant.RivamedLogRPCConstant;
import cn.rivamed.log.core.context.RivamedLogRPCContext;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 支持servlet
 * @author Bryan.Zhang
 * @since 1.3.5
 */
public class RivamedLogServletFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse){
            try{
                RivamedLogWebCommon.loadInstance().preHandle((HttpServletRequest)request);
                //把traceId放入response的header，为了方便有些人有这样的需求，从前端拿整条链路的traceId
                ((HttpServletResponse)response).addHeader(RivamedLogRPCConstant.RIVAMED_LOG_TRACE_KEY, RivamedLogRPCContext.getTraceId());
            }finally {
                RivamedLogWebCommon.loadInstance().afterCompletion();
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
