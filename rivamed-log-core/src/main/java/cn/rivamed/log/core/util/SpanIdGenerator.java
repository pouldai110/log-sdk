package cn.rivamed.log.core.util;

import cn.rivamed.log.core.context.RivamedLogRPCContext;
import com.alibaba.ttl.TransmittableThreadLocal;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * spanId生成器
 *
 * @author Bryan.Zhang
 * @since 1.0.0
 */
public class SpanIdGenerator {

    public static volatile TransmittableThreadLocal<String> currentSpanIdTL = new TransmittableThreadLocal<>();

    public static volatile TransmittableThreadLocal<AtomicInteger> spanIndex = new TransmittableThreadLocal<>();

    public static void putSpanId(String spanId) {
        if (StringUtils.isBlank(spanId)) {
            spanId = "0";
        }
        currentSpanIdTL.set(spanId);
        spanIndex.set(new AtomicInteger(0));
    }

    public static String getSpanId() {
        return currentSpanIdTL.get();
    }

    public static void removeSpanId() {
        currentSpanIdTL.remove();
    }

    public static String generateNextSpanId() {
        String currentSpanId = RivamedLogRPCContext.getSpanId();
        int currentSpanIndex = spanIndex.get().incrementAndGet();
        return MessageFormat.format("{0}.{1}", currentSpanId, currentSpanIndex);
    }
}
