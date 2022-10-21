package cn.rivamed.log.core.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WorkerPool;

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

        RingBuffer<LogMessageEvent> ringBuffer =
                RingBuffer.createMultiProducer(
                        () -> new LogMessageEvent(), 128 * 1024, new SleepingWaitStrategy());
        //2 通过ringBuffer 创建一个屏障
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        //3 创建含有10个消费者的数组:
        LogMessageConsumer[] consumers = new LogMessageConsumer[10];
        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new LogMessageConsumer("RivamedLogConsumer" + i);
        }
        //4 构建多消费者工作池
        WorkerPool<LogMessageEvent> workerPool = new WorkerPool<>(
                ringBuffer,
                sequenceBarrier,
                new EventExceptionHandler(),
                consumers);
        //5 设置多个消费者的sequence序号 用于单独统计消费进度, 并且设置到ringbuffer中
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
        //6 启动workerPool
        workerPool
                .start(Executors.newFixedThreadPool(10));
        return ringBuffer;
    }
}
