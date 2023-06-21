package cn.rivamed.log.core.disruptor;

import com.lmax.disruptor.ExceptionHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 描述: TODO
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @author 左健宏
 * @version V2.0.1
 * @date 23/05/29 16:24
 */
public class LogMessageExceptionHandler implements ExceptionHandler<Object> {

    private static final Logger LOGGER = Logger.getLogger(LogMessageExceptionHandler.class.getName());

    @Override
    public void handleEventException(Throwable ex, long sequence, Object event) {
        LOGGER.log(Level.SEVERE, "Disruptor Exception processing: " + sequence + " " + event, ex);
    }

    @Override
    public void handleOnStartException(Throwable ex) {
        LOGGER.log(Level.SEVERE, "Disruptor Exception during onStart()", ex);
    }

    @Override
    public void handleOnShutdownException(Throwable ex) {
        LOGGER.log(Level.SEVERE, "Disruptor Exception during onShutdown()", ex);
    }
}
