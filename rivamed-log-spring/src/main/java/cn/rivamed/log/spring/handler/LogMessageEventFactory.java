package cn.rivamed.log.spring.handler;

import cn.rivamed.log.core.event.LogMessageEvent;
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
