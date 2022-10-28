package cn.rivamed.log.core.disruptor;

import com.lmax.disruptor.EventHandler;

/**
 * @author weixiaobo
 * @date 2022年3月15日
 */
public class LogMessageEventCleanHandler implements EventHandler<LogMessageEvent> {

    @Override
    public void onEvent(LogMessageEvent event, long sequence, boolean endOfBatch) throws Exception {
        event.clear();
    }
}
