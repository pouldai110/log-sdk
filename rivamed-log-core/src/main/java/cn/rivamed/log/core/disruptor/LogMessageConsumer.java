package cn.rivamed.log.core.disruptor;

import cn.rivamed.log.core.client.AbstractClient;
import cn.rivamed.log.core.entity.BaseLogMessage;
import cn.rivamed.log.core.util.JsonUtil;
import com.lmax.disruptor.WorkHandler;

/**
 * className：LogMessageConsumer
 * description： 日志消费
 * time：2022-10-09.13:59
 *
 * @author Zuo Yang
 * @version 1.0.0
 */
public class LogMessageConsumer implements WorkHandler<LogMessageEvent> {

    private String name;

    public LogMessageConsumer(String name) {
        this.name = name;
    }

    @Override
    public void onEvent(LogMessageEvent event) throws Exception {
        BaseLogMessage baseLogMessage = event.getBaseLogMessage();
        AbstractClient.getClient().pushMessage(baseLogMessage.getLogType(), JsonUtil.toJSONString(baseLogMessage));
    }
}
