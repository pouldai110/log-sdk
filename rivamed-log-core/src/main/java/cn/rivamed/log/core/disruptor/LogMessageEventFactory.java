package cn.rivamed.log.core.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * (设备状态)事件工厂
 *
 * @author Zuo Yang
 * @date 2020/12/10
 */
public class LogMessageEventFactory implements EventFactory<LogMessageEvent> {

    @Override
    public LogMessageEvent newInstance() {
        return new LogMessageEvent();
    }
}
