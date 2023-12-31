package cn.rivamed.log.core.util;


import cn.rivamed.log.core.entity.BaseLogMessage;
import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * className：LogMessageThreadLocal
 * description：LogMessageThreadLocal 用来存储trace相关信息
 *
 * @author Zuo Yang
 * @version 1.0.0
 */
public class LogMessageThreadLocal {
    public static TransmittableThreadLocal<BaseLogMessage> logMessageThreadLocal = new TransmittableThreadLocal<>();
}
