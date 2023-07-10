package cn.rivamed.log.core.disruptor;

import cn.rivamed.log.core.client.AbstractClient;
import cn.rivamed.log.core.entity.BaseLogMessage;
import cn.rivamed.log.core.util.JsonUtil;
import com.lmax.disruptor.EventHandler;
import org.apache.commons.lang3.StringUtils;

/**
 * @author weixiaobo
 * @date 2022年3月15日
 */
public class LogMessageEventHandler implements EventHandler<LogMessageEvent> {

    @Override
    public void onEvent(LogMessageEvent event, long sequence, boolean endOfBatch) {
        try {
            BaseLogMessage baseLogMessage = event.getBaseLogMessage();
            AbstractClient client = AbstractClient.getClient();
            if (client != null) {
                String messageStr = JsonUtil.toJSONString(baseLogMessage);
                if (StringUtils.isNotBlank(messageStr)) {
                    client.pushMessage(messageStr);
                }
            }
        } finally {
            event.clear();
        }
    }

}
