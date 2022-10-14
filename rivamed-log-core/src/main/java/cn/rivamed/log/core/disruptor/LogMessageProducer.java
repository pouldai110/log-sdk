package cn.rivamed.log.core.disruptor;

import cn.rivamed.log.core.entity.BaseLogMessage;
import com.lmax.disruptor.RingBuffer;

/**
 * className：LogMessageProducer
 * description： 日志生产
 * time：2022-10-09.13:52
 *
 * @author Zuo Yang
 * @version 1.0.0
 */
public class LogMessageProducer {

    private RingBuffer<LogMessageEvent> ringBuffer;

    public LogMessageProducer(RingBuffer<LogMessageEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }


    public void send(BaseLogMessage data) {
        long next = ringBuffer.next();
        try {
            LogMessageEvent event = ringBuffer.get(next);
            event.setBaseLogMessage(data);
        } finally {
            ringBuffer.publish(next);
        }
    }
}
