package cn.rivamed.log.core.disruptor;

import cn.rivamed.log.core.entity.BaseLogMessage;

import java.io.Serializable;

/**
 * className：LogMessageEvent
 * description： TODO
 * time：2022-10-09.14:46
 *
 * @author Zuo Yang
 * @version 1.0.0
 */
public class LogMessageEvent implements Serializable {
    /**
     * 日志主体
     */

    private BaseLogMessage baseLogMessage;

    public LogMessageEvent() {

    }

    public BaseLogMessage getBaseLogMessage() {
        return baseLogMessage;
    }

    public void setBaseLogMessage(BaseLogMessage baseLogMessage) {
        this.baseLogMessage = baseLogMessage;
    }

    public void clear() {
        baseLogMessage = null;
    }
}
