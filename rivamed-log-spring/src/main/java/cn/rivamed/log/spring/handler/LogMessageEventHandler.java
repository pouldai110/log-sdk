package cn.rivamed.log.spring.handler;

import cn.rivamed.log.core.event.LogMessageEvent;
import cn.rivamed.log.spring.service.LogMessagePushService;
import com.lmax.disruptor.EventHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * @author Zuo Yang
 * @date 2022年3月15日
 */
@Component
public class LogMessageEventHandler implements EventHandler<LogMessageEvent> {

    @Resource
    LogMessagePushService logMessagePushService;

    @Override
    public void onEvent(LogMessageEvent event, long sequence, boolean endOfBatch) {
        logMessagePushService.sendMessage(event.getLogMessage());
    }

}
