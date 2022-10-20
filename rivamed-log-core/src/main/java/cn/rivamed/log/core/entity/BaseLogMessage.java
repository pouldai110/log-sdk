package cn.rivamed.log.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * className：BaseLogMessage
 * description：
 * time：2022-10-01.15:28
 *
 * @author Zuo Yang
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BaseLogMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 追踪码
     */
    private String traceId;

    /**
     * SPAN ID
     */
    private String spanId;

    /**
     * 系统名称
     */
    private String sysName;

    /**
     * 应用环境
     */
    private String env;

    /**
     * 类名
     */
    private String className;

    /**
     * 方法名
     */
    private String method;

    /**
     * 线程名
     */
    private String threadName;

    /**
     * 日志详情
     */
    private String bizDetail;

    /**
     * 堆栈信息
     */
    private String stackTrace;

    /**
     * 日志发生时间
     */
    private Date bizTime = new Date();

    /**
     * 执行机器ip
     */
    private String bizIP;

    /**
     * 日志级别
     */
    private String level;

    /**
     * 日志类型 logRecord/rabbitMQ/systemLog/loginLog
     */
    private String logType;

    /**
     * 当bizTime相同时服务端无法正确排序，因此需要增加一个字段保证相同毫秒的日志可正确排序
     */
    private Long seq;
}
