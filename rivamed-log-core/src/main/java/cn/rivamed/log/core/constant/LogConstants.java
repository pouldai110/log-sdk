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
public class LogConstants {

    public final static String LOG_TRACEID = "traceId";

    /**
     * 日志采集类型 http/rabbitmq
     */
    public final static String LOG_HTTP = "http";

    public final static String LOG_RABBITMQ = "rabbitmq";

    /**
     * 日志类型 logRecord/rabbitMQ/systemLog
     */
    public final static String LOG_TYPE_RECORD = "logRecord";

    public final static String LOG_TYPE_RABBITMQ = "rabbitMQ";

    public final static String LOG_TYPE_SYSTEMLOG = "systemLog";

    /**
     * record日志类型 timeTaskLog：定时任务 userLog：接口
     */
    public final static String LOG_Record_TYPE_SYSTEMLOG = "timeTaskLog";

    public final static String LOG_Record_TYPE_userLog = "userLog";
}
