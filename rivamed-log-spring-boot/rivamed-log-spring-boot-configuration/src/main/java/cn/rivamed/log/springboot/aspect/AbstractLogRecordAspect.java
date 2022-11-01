package cn.rivamed.log.springboot.aspect;

import brave.propagation.TraceContext;
import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.context.RivamedLogRecordContext;
import cn.rivamed.log.core.entity.LogRecordMessage;
import cn.rivamed.log.core.entity.TraceId;
import cn.rivamed.log.core.factory.MessageAppenderFactory;
import cn.rivamed.log.core.rpc.RivamedLogRecordHandler;
import cn.rivamed.log.core.util.IpGetter;
import cn.rivamed.log.core.util.JsonUtil;
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

    /**
     * 序列生成器：当日志在一毫秒内打印多次时，发送到服务端排序时无法按照正常顺序显示，因此加一个序列保证同一毫秒内的日志按顺序显示
     * 使用AtomicLong不要使用LongAdder，LongAdder在该场景高并发下无法严格保证顺序性，也不需要考虑Long是否够用，假设每秒打印10万日志，也需要两百多万年才能用的完
     */
    private static final AtomicLong SEQ_BUILDER = new AtomicLong(1);

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
            String cloneParams;
            try {
                cloneParams = JsonUtil.toJSONString(params);
            } catch (Exception e) {
                cloneParams = params.toString();
            }
            message.setMethod(joinPoint.getSignature().getDeclaringType().getSimpleName() + "." + m.getName());
            message.setUrl(request.getRequestURI());

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            returnValue = joinPoint.proceed(joinPoint.getArgs());
            stopWatch.stop();
            message.setSysName(RivamedLogRecordContext.getSysName());
            message.setEnv(RivamedLogRecordContext.getEnv());
            message.setClassName(ms.getMethod().getDeclaringClass().getName());
            message.setThreadName(Thread.currentThread().getName());
            message.setSeq(SEQ_BUILDER.getAndIncrement());
            message.setCostTime(stopWatch.getTime());
            message.setBizDetail(cloneParams);
            message.setLevel(LogLevel.INFO.name());
            message.setBizIP(IpGetter.CURRENT_IP);
            message.setLogType(LogMessageConstant.LOG_TYPE_RECORD);
            message.setLogRecordType(LogMessageConstant.LOG_RECORD_TYPE_USER_LOG);
            message.setResponseCode(String.valueOf(HttpStatus.OK.value()));
            //设置额外信息并推送消息
            RivamedLogRecordContext.buildLogMessage(message);
            MessageAppenderFactory.push(message);
            return returnValue;
        } finally {
            cleanThreadLocal();
        }
    }
}
