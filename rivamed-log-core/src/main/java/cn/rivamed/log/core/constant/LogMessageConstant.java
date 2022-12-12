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

    public final static String MESSAGE_ID = "messageId";

    public final static String SUCCESS = "1";

    public final static String FAIL = "0";

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
    public final static String LOG_TYPE_RECORD_LOG = "logRecord";

    public final static String LOG_TYPE_RABBITMQ_LOG = "rabbitMQ";

    public final static String LOG_TYPE_SYSTEM_LOG = "systemLog";

    public final static String LOG_TYPE_LOGIN_LOG = "loginLog";

    public final static String LOG_TYPE_SCHEDULED_TASK_LOG = "scheduledTask";

    /**
     * 语义化日志类型 system/swagger/mztbiz
     */
    public final static String LOG_RECORD_TYPE_SYSTEM = "system";

    public final static String LOG_RECORD_TYPE_SWAGGER = "swagger";

    public final static String LOG_RECORD_TYPE_MZTBIZ = "mztbiz";


    public final static String DELIM_STR = "{}";


    public final static String IS_UNDEFINED = "IS_UNDEFINED";


    public final static String EXCEPTION_CONFIG = "${";

    /**
     * SQL 打印日志前缀
     */
    public static final String LEFT_EQUALS = "<==";

    public static final String RIGHT_EQUALS = "==>";

    public static final String HIBERNATE_SQL_PATH = "org.hibernate.SQL";


    /**
     * 发送 消息类型
     */
    public final static String MESSAGE_TYPE_SEND = "send";

    /**
     * 接收 消息类型
     */
    public final static String MESSAGE_TYPE_ACCEPT = "accept";

    /**
     * SWAGGER 切面增强常量
     */
    public static final String API_OPERATION_CLASS_NAME = "io.swagger.annotations.ApiOperation";

    public static final String API_OPERATION_FIELD_NAME = "value";


    /**
     * 服务器IP
     */
    public static final String RIVAMED_LOG_SERVER_IP = "rivamed.log.serverIp";

    /**
     * 日志客户端注册队列
     */
    public static final String RIVAMED_LOG_REG_QUEUE_NAME = "rivamed.log.client.reg";

}
