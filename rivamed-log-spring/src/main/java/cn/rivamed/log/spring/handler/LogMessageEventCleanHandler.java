package cn.rivamed.log.spring.handler;

import cn.rivamed.log.core.event.LogMessageEvent;
import com.lmax.disruptor.EventHandler;
import org.springframework.stereotype.Component;

/**
 * @author Zuo Yang
 * @date 2022年3月15日
 */
@Component
public class LogMessageEventCleanHandler implements EventHandler<LogMessageEvent> {

    @Override
    public void onEvent(LogMessageEvent event, long sequence, boolean endOfBatch) throws Exception {
        event.clear();
    }
}
