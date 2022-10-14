package cn.rivamed.log.core.exception;


/**
 * 连接日志队列异常抛出，redis挂了，或者kafka挂了，或者rabbitmq挂了
 */
public class LogQueueConnectException extends Exception {

    public LogQueueConnectException() {
        super();
    }

    public LogQueueConnectException(String message) {
        super(message);
    }

    public LogQueueConnectException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogQueueConnectException(Throwable cause) {
        super(cause);
    }
}
