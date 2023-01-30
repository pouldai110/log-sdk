package cn.rivamed.log.task.spring;

import brave.Span;
import brave.Tracer;
import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.context.RivamedLogContext;
import cn.rivamed.log.core.util.DateUtil;
import cn.rivamed.log.core.util.LogTemplateUtil;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * 基于spring scheduled定时器的增强AOP
 *
 * @author Zuo Yang
 */
@Aspect
public class SpringScheduledTaskAop {

    private static final Logger logger = LoggerFactory.getLogger(SpringScheduledTaskAop.class);

    private final Tracer tracer;

    public SpringScheduledTaskAop(Tracer tracer) {
        this.tracer = tracer;
    }

    @Around("execution (@org.springframework.scheduling.annotation.Scheduled  * *.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        //只有新增一个trace, 不然定时任务是同一个线程导致TraceId一直一样
        Span span = tracer.newTrace();
        tracer.withSpanInScope(span);
        //执行时间
        String dateStr = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        Method m = ms.getMethod();
        //执行方法
        String method = pjp.getSignature().getDeclaringType().getSimpleName() + "." + m.getName();
        Object proceed;
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        boolean taskEnable = RivamedLogContext.isLogEnable() && RivamedLogContext.isTaskEnable();
        try {
            proceed = pjp.proceed();
            stopWatch.stop();
            if (taskEnable) {
                logger.info(LogMessageConstant.LOG_TYPE_SCHEDULED_TASK_LOG + String.format(LogTemplateUtil.TASK_SUCCESS_FORMAT, dateStr, method, stopWatch.getTime()));
            }
            return proceed;
        } catch (Throwable ex) {
            String message = ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
            if (taskEnable) {
                logger.error(LogMessageConstant.LOG_TYPE_SCHEDULED_TASK_LOG + String.format(LogTemplateUtil.TASK_FAIL_FORMAT, dateStr, method), message);
            }
            throw ex;
        } finally {
            span.finish();
        }
    }

}
