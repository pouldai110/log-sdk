package cn.rivamed.log.task.spring;

import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.util.DateUtil;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * 基于spring scheduled定时器的增强AOP
 *
 * @author Bryan.Zhang
 * @since 1.3.4
 */
@Aspect
public class SpringScheduledTaskAop {

    private static final Logger logger = LoggerFactory.getLogger(SpringScheduledTaskAop.class);

    private static final String taskNormalFormatStr = " 在 %s 自动执行了 %s 方法，耗时%s 毫秒";
    private static final String taskErrorFormatStr = " 在 %s 自动执行 %s 方法失败";


    @Pointcut("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void cut() {
    }

    @Around("cut()")
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        //执行时间
        String dateStr = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
        MethodSignature ms = (MethodSignature) jp.getSignature();
        Method m = ms.getMethod();
        //执行方法
        String method = jp.getSignature().getDeclaringType().getSimpleName() + "." + m.getName();
        Object proceed;
        try {
            proceed = jp.proceed();
            stopWatch.stop();
            logger.info(LogMessageConstant.LOG_TYPE_SCHEDULED_TASK_LOG + String.format(taskNormalFormatStr, dateStr, method, stopWatch.getTime()));
        } catch (Exception e) {
            logger.error(LogMessageConstant.LOG_TYPE_SCHEDULED_TASK_LOG + String.format(taskErrorFormatStr, dateStr, method), e.getMessage());
            throw e;
        }
        return proceed;
    }
}
