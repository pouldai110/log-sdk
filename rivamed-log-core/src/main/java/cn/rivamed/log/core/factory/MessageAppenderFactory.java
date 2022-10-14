package cn.rivamed.log.core.factory;

import cn.rivamed.log.core.client.AbstractClient;
import cn.rivamed.log.core.disruptor.LogMessageProducer;
import cn.rivamed.log.core.disruptor.LogMessageRingBuffer;
import cn.rivamed.log.core.entity.BaseLogMessage;
import cn.rivamed.log.core.exception.LogQueueConnectException;
import cn.rivamed.log.core.util.GfJsonUtil;
import cn.rivamed.log.core.util.HttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * className：MessageAppenderFactory
 * description： TODO
 * time：2022-10-03.14:18
 *
 * @author Zuo Yang
 * @version 1.0.0
 */
public class MessageAppenderFactory {


    public static void push(BaseLogMessage baseLogMessage) {
        LogMessageProducer producer = new LogMessageProducer(LogMessageRingBuffer.ringBuffer);
        producer.send(baseLogMessage);
    }

    private static void push(String rivamedLogHost, String key, List<String> baseLogMessage, String logOutPutKey) {
        if (baseLogMessage.size() == 0) {
            return;
        }
        List<Map<String, Object>> logs = new ArrayList<>();
        for (String str : baseLogMessage) {
            Map<String, Object> map = GfJsonUtil.parseObject(str, Map.class);
            logs.add(map);
        }

        try {
            String url = "http://" + rivamedLogHost;
            String param = GfJsonUtil.toJSONString(logs);
            HttpClient.doPostBody(url, param);
        } catch (Exception e) {
            System.out.println("Rivamed Log error:----------------" + e.getMessage() + "-------------------");
        }
    }

}
