package cn.rivamed.log.web.filter;

import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.entity.TraceId;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * TraceIdFilter
 */
public class TraceIdFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String traceId = request.getParameter(LogMessageConstant.TRACE_ID);
            if (StringUtils.isBlank(traceId)) {
                TraceId.set();
            } else {
                TraceId.logTraceID.set(traceId);
            }
        } finally {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
    }
}
