package cn.rivamed.log.core.event;

/**
 * 描述: TODO
 * 公司 北京瑞华康源科技有限公司
 * 版本 rivamed2020
 *
 * @version V2.0.1
 * @author: Zuo Yang
 * @date: 22/06/14 17:05
 */
public class LogMessageEvent {

    private Object logMessage;

    public Object getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(Object logMessage) {
        this.logMessage = logMessage;
    }

    public void clear() {
        logMessage = null;
    }
}
