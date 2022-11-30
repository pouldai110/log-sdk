package cn.rivamed.log.springboot.handler;

import cn.rivamed.log.core.context.RivamedLogContext;
import cn.rivamed.log.core.entity.RivamedLogRecordLabel;
import cn.rivamed.log.core.entity.TraceId;
import cn.rivamed.log.core.rpc.RivamedLogRecordHandler;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.mzt.logapi.starter.support.parse.LogRecordValueParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Rivamed Log的RPC处理逻辑的封装类
 *
 * @author Zuo Yang
 * @since 1.2.0
 */
public class RivamedMztBizLogRecordHandler extends LogRecordValueParser {

    protected static final Logger log = LoggerFactory.getLogger(RivamedLogRecordHandler.class);

    public void processProviderSide(RivamedLogRecordLabel labelBean) {
        TransmittableThreadLocal<RivamedLogRecordLabel> rivamedLogLabelTTL = RivamedLogContext.rivamedLogLabelTTL;

        rivamedLogLabelTTL.set(labelBean);
    }

    public void cleanThreadLocal() {
        //移除ThreadLocal里的数据
        RivamedLogContext.rivamedLogLabelTTL.remove();

        TraceId.logTraceID.remove();
        TraceId.logSpanID.remove();
    }
}
