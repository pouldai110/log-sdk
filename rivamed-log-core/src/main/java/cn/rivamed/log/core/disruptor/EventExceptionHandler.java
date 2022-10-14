package cn.rivamed.log.core.disruptor;

import com.lmax.disruptor.ExceptionHandler;

/**
 * className：EventExceptionHandler
 * description： TODO
 * time：2022-10-09.15:28
 *
 * @author Zuo Yang
 * @version 1.0.0
 */
public class EventExceptionHandler implements ExceptionHandler<LogMessageEvent> {
    @Override
    public void handleEventException(Throwable ex, long sequence, LogMessageEvent event) {

    }

    @Override
    public void handleOnStartException(Throwable ex) {

    }

    @Override
    public void handleOnShutdownException(Throwable ex) {

    }
}
