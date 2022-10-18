package cn.rivamed.log.core.entity;


import cn.rivamed.log.core.util.IdWorker;
import cn.rivamed.log.core.util.TraceIdGenerator;
import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * className：TraceId
 * description：TraceId 用来存储traceID相关信息
 * @author Zuo Yang
 * @version 1.0.0
 */
public class TraceId {
    public static TransmittableThreadLocal<String> logTraceID = new TransmittableThreadLocal<String>();
    public static IdWorker worker = new IdWorker(1, 1, 1);

    public static void set() {
        logTraceID.set(String.valueOf(worker.nextId()));
    }

    public static void setSofa() {
        String traceId = TraceIdGenerator.generate();
        logTraceID.set(traceId);
    }

    public static String getTraceId(String traceId) {
        if (traceId == null || traceId.equals("")) {
            traceId = String.valueOf(worker.nextId());
            TraceId.logTraceID.set(traceId);
        }
        return traceId;
    }
}
