package cn.rivamed.log.core.entity;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class RivamedLogTraceIdInterceptor extends HandlerInterceptorAdapter{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String traceId = request.getHeader("traceId");
        if (StringUtils.isEmpty(traceId)){
            TraceId.set();
        }else{
            TraceId.logTraceID.set(traceId);
        }
        return true;
    }

}
