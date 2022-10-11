package cn.rivamed.log.spring.service;

import cn.rivamed.log.core.constant.LogConstants;
import cn.rivamed.log.core.entity.SystemLogMessage;
import cn.rivamed.log.core.event.LogMessageEvent;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;
import org.apache.logging.log4j.core.LogEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 推送服务
 *
 * @author Zuo Yang
 * @date 2022/10/01
 */
@Service
public class DisruptorPushService {

    @Resource
    private Disruptor disruptor;

    @Resource
    private DisruptorPushService disruptorPushService;

    public void publishSystemLogMessage(LogEvent logEvent, String sysName) {
        SystemLogMessage logMessage = new SystemLogMessage();
        logMessage.setLevel(logEvent.getLevel().name());
        logMessage.setTraceId(logEvent.getContextData().getValue(LogConstants.LOG_TRACEID));
        logMessage.setSpanId(logEvent.getContextData().getValue(LogConstants.LOG_SPANID));
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
