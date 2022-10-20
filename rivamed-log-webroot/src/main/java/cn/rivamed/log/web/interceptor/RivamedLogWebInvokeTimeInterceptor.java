package cn.rivamed.log.web.interceptor;

import cn.rivamed.log.core.context.RivamedLogRPCContext;
import cn.rivamed.log.core.util.JsonUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * RIVAMED LOG web的调用时间统计拦截器
 *
 * @author Bryan.Zhang
 * @since 1.2.0
 */
public class RivamedLogWebInvokeTimeInterceptor extends AbstractRivamedLogWebHandlerMethodInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RivamedLogWebInvokeTimeInterceptor.class);

    private final TransmittableThreadLocal<StopWatch> invokeTimeTL = new TransmittableThreadLocal<>();

    @Override
    public boolean preHandleByHandlerMethod(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (RivamedLogRPCContext.enableInvokeTimePrint()) {
            String url = request.getRequestURI();

            // 打印请求参数
            // 先屏蔽掉打印body的功能，目前只打印get参数
            /*if (isJson(request)) {
                String jsonParam = new RequestWrapper(request).getBodyString();
                log.info("[RIVAMED LOG]开始请求URL[{}],参数为:{}", url, jsonParam);
            } else {
                String parameters = JSONUtil.toJsonStr(request.getParameterMap());
                log.info("[RIVAMED LOG]开始请求URL[{}],参数为:{}", url, parameters);
            }*/

            String parameters = JsonUtil.toJSONString(request.getParameterMap());
            log.info("[RIVAMED LOG]开始请求URL[{}],参数为:{}", url, parameters);

            StopWatch stopWatch = new StopWatch();
            invokeTimeTL.set(stopWatch);
            stopWatch.start();
        }
        return true;
    }

    @Override
    public void postHandleByHandlerMethod(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletionByHandlerMethod(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (RivamedLogRPCContext.enableInvokeTimePrint()) {
            StopWatch stopWatch = invokeTimeTL.get();
            stopWatch.stop();
            log.info("[RIVAMED LOG]结束URL[{}]的调用,耗时为:{}毫秒", request.getRequestURI(), stopWatch.getTime());
            invokeTimeTL.remove();
        }
    }

    /**
     * 判断本次请求的数据类型是否为json
     *
     * @param request request
     * @return boolean
     */
    private boolean isJson(HttpServletRequest request) {
        if (request.getContentType() != null) {
            return request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE) ||
                    request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE);
        }
        return false;
    }
}
