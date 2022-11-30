package cn.rivamed.log.task.quartz;

import brave.Span;
import brave.Tracer;
import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.context.RivamedLogContext;
import cn.rivamed.log.core.spring.RivamedLogApplicationContextHolder;
import cn.rivamed.log.core.util.DateUtil;
import cn.rivamed.log.core.util.LogTemplateUtil;
import org.apache.commons.lang3.time.StopWatch;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.JobDetailImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

public abstract class RivamedLogQuartzJobBean extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(RivamedLogQuartzJobBean.class);

    private static Tracer tracer;


    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (tracer == null) {
            tracer = RivamedLogApplicationContextHolder.getApplicationContext().getBean("tracer", Tracer.class);
        }
        Span span = tracer.newTrace();
        tracer.withSpanInScope(span);
        //执行时间
        String dateStr = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
        //执行方法
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String method = ((JobDetailImpl) jobExecutionContext.getJobDetail()).getFullName();
        try {
            executeTask(jobExecutionContext);
            stopWatch.stop();
            if (RivamedLogContext.isTaskEnable()) {
                logger.info(LogMessageConstant.LOG_TYPE_SCHEDULED_TASK_LOG + String.format(LogTemplateUtil.TASK_SUCCESS_FORMAT, dateStr, method, stopWatch.getTime()));
            }
        } catch (Throwable ex) {
            String message = ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
            if (RivamedLogContext.isTaskEnable()) {
                logger.error(LogMessageConstant.LOG_TYPE_SCHEDULED_TASK_LOG + String.format(LogTemplateUtil.TASK_FAIL_FORMAT, dateStr, method), message);
            }
            throw ex;
        } finally {
            span.finish();
        }
    }

    public abstract void executeTask(JobExecutionContext jobExecutionContext) throws JobExecutionException;
}
