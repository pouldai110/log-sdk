package cn.rivamed.log.spring.component;

import cn.rivamed.log.core.constant.LogConstants;
import cn.rivamed.log.core.entity.SystemLogMessage;
import cn.rivamed.log.core.event.LogMessageEvent;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;
import org.apache.logging.log4j.core.LogEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DisruptorPushService {

    @Resource
    private Disruptor disruptor;

    @Resource
    private DisruptorPushService disruptorPushService;

    public void publishMessage(LogEvent logEvent, String sysName) {
        SystemLogMessage logMessage = new SystemLogMessage();
        logMessage.setLevel(logEvent.getLevel().name());
        logMessage.setTraceId(logEvent.getContextData().getValue("traceId"));
        logMessage.setBizDetail(logEvent.getMessage().getFormattedMessage());
        logMessage.setMethod(logEvent.getSource().getMethodName());
        logMessage.setSysName(sysName);
        logMessage.setLogType(LogConstants.LOG_TYPE_SYSTEMLOG);
        disruptorPushService.publishEvent((event, sequence) -> event.setLogMessage(logMessage));
    }

    public void publishEvent(EventTranslator<LogMessageEvent> translator) {
        disruptor.getRingBuffer().publishEvent(translator);
    }

}
