package cn.rivamed.log.core.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WorkerPool;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.Executors;

/**
 * className：LogMessageRingBuffer
 * description： TODO
 * time：2022-10-09.15:26
 *
 * @author Zuo Yang
 * @version 1.0.0
 */
public class LogMessageRingBuffer {

    public static RingBuffer<LogMessageEvent> ringBuffer = getRingBuffer();

    private static RingBuffer<LogMessageEvent> getRingBuffer() {
        LogMessageEventFactory eventFactory = new LogMessageEventFactory();
        // ringBuffer大小必须为2的倍数
        Disruptor<LogMessageEvent> disruptor = new Disruptor<>(eventFactory, 128 * 1024, new CustomizableThreadFactory("event-handler-"),
                ProducerType.SINGLE, new SleepingWaitStrategy());
        // 连接消费端
        disruptor.handleEventsWith(new LogMessageEventHandler()).then(new LogMessageEventCleanHandler());
        disruptor.start();
        return disruptor.getRingBuffer();
    }
}
