package cn.rivamed.log.task.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public abstract class TLogQuartzJobBean extends QuartzJobBean {


    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }

    public abstract void executeTask(JobExecutionContext jobExecutionContext) throws JobExecutionException;
}
