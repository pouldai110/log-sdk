package cn.rivamed.log.core.constant;

/**
 * 描述: 公共常量
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @author Zuo Yang
 * @version V2.0.1
 * @date 22/09/28 10:11
 */
public class LogMessageConstant {

    public final static String TRACE_ID = "traceId";

    public final static String SPAN_ID = "spanId";

    /**
     * 链路日志前缀
     */
    public final static String TRACE_PRE = "TRACE:";

    public final static String LOG_KEY = "rivamed_log_list";

    public final static String LOG_KEY_COMPRESS = "rivamed_log_list_compress";

    /**
     * 链路日志存入ES的索引后缀
     */
    public final static String LOG_KEY_TRACE = "rivamed_trace_list";

    public final static String LOG_KEY_TRACE_COMPRESS = "rivamed_trace_list_compress";


    /**
     * 日志采集类型 http/rabbitmq
     */
    public final static String LOG_HTTP = "http";

    public final static String LOG_RABBITMQ = "rabbitmq";

    /**
     * 日志类型 logRecord/rabbitMQ/systemLog/loginLog
     */
    public final static String LOG_TYPE_RECORD = "logRecord";

    public final static String LOG_TYPE_RABBITMQ = "rabbitMQ";

    public final static String LOG_TYPE_SYSTEM_LOG = "systemLog";

    public final static String LOG_TYPE_LOGIN_LOG = "loginLog";

    public final static String LOG_TYPE_SCHEDULED_TASK_LOG = "scheduledTask";

    public final static String DELIM_STR = "{}";


    public final static String IS_UNDEFINED = "IS_UNDEFINED";


    public final static String EXCEPTION_CONFIG = "${";


    /**
     * 发送 消息类型
     */
    public final static String MESSAGE_TYPE_SEND = "send";

    /**
     * 接收 消息类型
     */
    public final static String MESSAGE_TYPE_ACCEPT = "accept";

}
