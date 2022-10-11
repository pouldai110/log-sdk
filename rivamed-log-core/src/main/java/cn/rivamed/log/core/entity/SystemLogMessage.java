package cn.rivamed.log.core.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述: 系统日志
 * 公司: 北京瑞华康源科技有限公司<br/>
 * 版权: rivamed2020<br/>
 *
 * @author fujiali
 * @version V1.0
 * @date 2022/9/27 15:30
 */
public class SystemLogMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    private String id;

    /**
     * 日志发生时间
     */
    private Date bizTime = new Date();

    /**
     * 日志详情
     */
    private String bizDetail;

    /**
     * 接口执行时长
     */
    private Long costTime;

    /**
     * 日志级别
     */
    private String level;

    /**
     * traceId
     */
    private String traceId;

    /**
     * spanId
     */
    private String spanId;

    /**
     * 登录tokenId
     */
    private String tokenId;

    /**
     * 系统方法名
     */
    private String method;

    /**
     * 日志类型 logRecord/rabbitMQ/systemLog
     */
    private String logType;

    /**
     * 系统名称
     */
    private String sysName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getBizTime() {
        return bizTime;
    }

    public void setBizTime(Date bizTime) {
        this.bizTime = bizTime;
    }

    public String getBizDetail() {
        return bizDetail;
    }

    public void setBizDetail(String bizDetail) {
        this.bizDetail = bizDetail;
    }

    public Long getCostTime() {
        return costTime;
    }

    public void setCostTime(Long costTime) {
        this.costTime = costTime;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    @Override
    public String toString() {
        return "SystemLogMessage{" +
                "id='" + id + '\'' +
                ", bizTime=" + bizTime +
                ", bizDetail='" + bizDetail + '\'' +
                ", costTime=" + costTime +
                ", level='" + level + '\'' +
                ", traceId='" + traceId + '\'' +
                ", spanId='" + spanId + '\'' +
                ", tokenId='" + tokenId + '\'' +
                ", method='" + method + '\'' +
                ", logType='" + logType + '\'' +
                ", sysName='" + sysName + '\'' +
                '}';
    }

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

}
