package cn.rivamed.log.core.entity;

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
     * 日志类型 logRecord/rabbitMQ/systemLog
     */
    private String logType;

    /**
     * 当bizTime相同时服务端无法正确排序，因此需要增加一个字段保证相同毫秒的日志可正确排序
     */
    private Long seq;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getBizDetail() {
        return bizDetail;
    }

    public void setBizDetail(String bizDetail) {
        this.bizDetail = bizDetail;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public Date getBizTime() {
        return bizTime;
    }

    public void setBizTime(Date bizTime) {
        this.bizTime = bizTime;
    }

    public String getBizIP() {
        return bizIP;
    }

    public void setBizIP(String bizIP) {
        this.bizIP = bizIP;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }
}
