package cn.rivamed.log.core.entity;

import lombok.*;
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
     * 子系统名称
     */
    private String subSystemName;

    /**
     * 类名
     */
    private String className;

    /**
     * 方法名
     */
    private String method;

    /**
     * 方法描述
     */
    private String methodDesc;

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
     * 日志类型 1:接口日志/2：rabbitMQ/3:运行日志/4：定时任务
     */
    private String logType;

    /**
     * 当bizTime相同时服务端无法正确排序，因此需要增加一个字段保证相同毫秒的日志可正确排序
     */
    private Long seq;
}
