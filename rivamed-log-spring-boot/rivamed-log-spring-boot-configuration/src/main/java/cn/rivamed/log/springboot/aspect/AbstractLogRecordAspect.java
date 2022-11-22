package cn.rivamed.log.springboot.aspect;

import brave.propagation.TraceContext;
import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.context.RivamedLogContext;
import cn.rivamed.log.core.entity.LogRecordMessage;
import cn.rivamed.log.core.entity.TraceId;
import cn.rivamed.log.core.factory.MessageAppenderFactory;
import cn.rivamed.log.core.rpc.RivamedLogRecordHandler;
import cn.rivamed.log.core.util.IpGetter;
import cn.rivamed.log.core.util.JsonUtil;
import cn.rivamed.log.core.util.LogTemplateUtil;
import cn.rivamed.log.core.util.RivamedClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


/**
 * className：AbstractLogRecordAspect
 * description： 链路追踪打点拦截
 * time：2022-10-14.11:17
 *
 * @author Zuo Yang
 * @version 1.2.0
 */
public abstract class AbstractLogRecordAspect extends RivamedLogRecordHandler {

    private static Logger logger = LoggerFactory.getLogger(RivamedLogRecordHandler.class);

    private static final String API_OPERATION_CLASS_NAME = "io.swagger.annotations.ApiOperation";
    private static final String API_OPERATION_FIELD_NAME = "value";

    public Object aroundExecute(ProceedingJoinPoint joinPoint) throws Throwable {
        LogRecordMessage message = new LogRecordMessage();
        String method = null;
        String methodDesc = null;

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Object returnValue;
            final List<Object> params = new ArrayList<>();
            ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (sra == null) {
                return null;
            }
            HttpServletRequest request = sra.getRequest();
            TraceContext context = (TraceContext) request.getAttribute(TraceContext.class.getName());
            message.setTraceId(context.traceIdString());
            message.setSpanId(context.spanIdString());
            TraceId.logTraceID.set(context.traceIdString());
            TraceId.logSpanID.set(context.spanIdString());
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
            methodDesc = method = joinPoint.getSignature().getDeclaringType().getSimpleName() + "." + m.getName();
            String value = RivamedClassUtils.getAnnotationValue(API_OPERATION_CLASS_NAME, m, API_OPERATION_FIELD_NAME);
            if (StringUtils.isNotBlank(value)) {
                methodDesc = value;
            }
            String cloneParams;
            try {
                cloneParams = JsonUtil.toJSONString(params);
            } catch (Exception e) {
                cloneParams = params.toString();
            }
            if (RivamedLogContext.isRequestEnable()) {
                logger.info(request.getRequestURI() + " param: {}", cloneParams);
            }
            message.setMethod(method);
            message.setUrl(request.getRequestURI());
            message.setSysName(RivamedLogContext.getSysName());
            message.setEnv(RivamedLogContext.getEnv());
            message.setClassName(ms.getMethod().getDeclaringClass().getName());
            message.setThreadName(Thread.currentThread().getName());
            message.setBizIP(IpGetter.CURRENT_IP);
            message.setLogType(LogMessageConstant.LOG_TYPE_RECORD);

            returnValue = joinPoint.proceed(joinPoint.getArgs());
            String result;
            try {
                result = JsonUtil.toJSONString(returnValue);
            } catch (Exception e) {
                result = returnValue.toString();
            }
            if (RivamedLogContext.isResponseEnable()) {
                logger.info(request.getRequestURI() + " result: {}", result);
            }
            message.setLevel(LogLevel.INFO.name());
            message.setResponseCode(String.valueOf(HttpStatus.OK.value()));
            message.setBizDetail(String.format(LogTemplateUtil.LOG_RECORD_SUCCESS_FORMAT, methodDesc, stopWatch.getTime()));
            return returnValue;
        } catch (Exception e) {
            message.setLevel(LogLevel.ERROR.name());
            message.setBizDetail(String.format(LogTemplateUtil.LOG_RECORD_FAIL_FORMAT, methodDesc));
            message.setResponseCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            throw e;
        } finally {
            stopWatch.stop();
            message.setCostTime(stopWatch.getTime())
            .setBizTime(new Date());
            //设置额外信息并推送消息
            RivamedLogContext.buildLogMessage(message);
            MessageAppenderFactory.push(message);
            cleanThreadLocal();
        }
    }
}
