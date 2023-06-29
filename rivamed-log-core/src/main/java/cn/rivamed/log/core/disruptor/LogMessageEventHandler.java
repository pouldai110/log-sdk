package cn.rivamed.log.core.disruptor;

import cn.rivamed.log.core.client.AbstractClient;
import cn.rivamed.log.core.entity.BaseLogMessage;
import cn.rivamed.log.core.util.JsonUtil;
import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author weixiaobo
 * @date 2022年3月15日
 */
@Slf4j
public class LogMessageEventHandler implements EventHandler<LogMessageEvent> {

    @Override
    public void onEvent(LogMessageEvent event, long sequence, boolean endOfBatch) {
        try {
            BaseLogMessage baseLogMessage = event.getBaseLogMessage();
            AbstractClient client = AbstractClient.getClient();
            if (client != null) {
                client.pushMessage(JsonUtil.toJSONString(baseLogMessage));
            }
        } catch (Exception e){
            log.info("data handle error: {}, data {}", e.getMessage(),JsonUtil.toJSONString(event.getBaseLogMessage()));
            e.printStackTrace();
        }
        finally {
            event.clear();
        }
    }

}
