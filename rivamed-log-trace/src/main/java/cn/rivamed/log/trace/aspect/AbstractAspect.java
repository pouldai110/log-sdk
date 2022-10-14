package cn.rivamed.log.trace.aspect;

import brave.propagation.TraceContext;
import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.context.RivamedLogContext;
import cn.rivamed.log.core.context.RivamedLogLabel;
import cn.rivamed.log.core.entity.LogRecordMessage;
import cn.rivamed.log.core.factory.MessageAppenderFactory;
import cn.rivamed.log.core.rpc.RivamedLogRPCHandler;
import cn.rivamed.log.core.util.GfJsonUtil;
import cn.rivamed.log.core.util.IpGetter;
import com.alibaba.ttl.TransmittableThreadLocal;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * className：AbstractAspect
 * description： 链路追踪打点拦截
 * time：2022-10-14.11:17
 *
 * @author Zuo Yang
 * @version 1.2.0
 */
public abstract class AbstractAspect extends RivamedLogRPCHandler {

    public Object aroundExecute(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object returnValue;
            final List<Object> params = new ArrayList<>();
            ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (sra == null) {
                return null;
            }
            LogRecordMessage message = new LogRecordMessage();
            HttpServletRequest request = sra.getRequest();
            TraceContext context = (TraceContext) request.getAttribute("brave.propagation.TraceContext");
            message.setTraceId(context.traceIdString());
            message.setSpanId(context.spanIdString());
            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < args.length; i++) {
                Object object = args[i];
                if (object instanceof HttpServletResponse || object instanceof HttpServletRequest) {
                    continue;
                }
                params.add(object);
            }
            MethodSignature ms = (MethodSignature) joinPoint.getSignature();
            Method m = ms.getMethod();
            String cloneParams;
            try {
                cloneParams = GfJsonUtil.toJSONString(params);
            } catch (Exception e) {
                cloneParams = params.toString();
            }
            message.setMethod(joinPoint.getSignature().getDeclaringType().getSimpleName() + "." + m.getName());
            message.setUrl(request.getRequestURI());

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            returnValue = joinPoint.proceed(joinPoint.getArgs());
            stopWatch.stop();
            message.setCostTime(stopWatch.getTime());
            message.setBizDetail(cloneParams);
            message.setLevel(LogLevel.INFO.name());
            message.setResponseCode(HttpStatus.OK.name());
            message.setBizIP(IpGetter.CURRENT_IP);
            message.setLogType(LogMessageConstant.LOG_TYPE_RECORD);
            message.setLogRecordType(LogMessageConstant.LOG_RECORD_TYPE_USER_LOG);
            message.setResponseCode(String.valueOf(HttpStatus.OK.value()));
            //设置额外信息并推送消息
            RivamedLogContext.buildLogMessage(message);
            MessageAppenderFactory.push(message);
            return returnValue;
        } finally {
            cleanThreadLocal();
        }
    }
}
