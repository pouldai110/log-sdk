package cn.rivamed.log.task.spring;

import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.util.JsonUtil;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

/**
 * 基于spring scheduled定时器的增强AOP
 *
 * @author Bryan.Zhang
 * @since 1.3.4
 */
@Aspect
@Component
@ConditionalOnClass(org.springframework.scheduling.annotation.Scheduled.class)
public class SpringScheduledTaskAop {

    private static final Logger logger = LoggerFactory.getLogger(SpringScheduledTaskAop.class);


    @Pointcut("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void cut() {
    }

    @Around("cut()")
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object proceed = jp.proceed();
        stopWatch.stop();
        System.out.println(stopWatch.getTime());
        logger.info(LogMessageConstant.LOG_TYPE_SCHEDULED_TASK_LOG, JsonUtil.toJSONString(proceed));
        return proceed;
    }
}
