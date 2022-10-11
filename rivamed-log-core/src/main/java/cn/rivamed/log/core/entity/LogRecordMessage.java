package cn.rivamed.log.core.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述: 接口、定时任务日志信息
 * 公司: 北京瑞华康源科技有限公司<br/>
 * 版权: rivamed2020<br/>
 *
 * @author fujiali
 * @version V1.0
 * @date 2022/9/27 15:34
 */
public class LogRecordMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 语义日志id
     */
    private String id;

    /**
     * 日志发生时间
     */
    private Date bizTime = new Date();

    /**
     * 操作人账号
     */
    private String userId;

    /**
     * 操作人姓名
     */
    private String userName = "";

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 设备sn
     */
    private String sn;

    /**
     * 访问URL
     */
    private String url;

    /**
     * 系统方法名
     */
    private String method;

    /**
     * 业务号
     */
    private String bizId;

    /**
     * 业务产品名
     */
    private String bizProd;

    /**
     * 业务动作
     */
    private String bizAction;

    /**
     * 系统名称
     */
    private String sysName;

    /**
     * 执行机器ip
     */
    private String bizIP;

    /**
     * 日志详情
     */
    private String bizDetail;

    /**
     * 接口执行时长
     */
    private Long costTime;

    /**
     * 操作状态码
     */
    private String responseCode;

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
     * LogRecode(语义化日志字符串)
     */
    private String logRecord;

    /**
     * 堆栈信息
     */
    private String stackTrace;

    /**
     * record日志类型 timeTaskLog：定时任务 userLog：接口
     */
    private String logRecordType;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 日志类型 logRecord/rabbitMQ/systemLog
     */
    private String logType;


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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getBizProd() {
        return bizProd;
    }

    public void setBizProd(String bizProd) {
        this.bizProd = bizProd;
    }

    public String getBizAction() {
        return bizAction;
    }

    public void setBizAction(String bizAction) {
        this.bizAction = bizAction;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getBizIP() {
        return bizIP;
    }

    public void setBizIP(String bizIP) {
        this.bizIP = bizIP;
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

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
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

    public String getLogRecord() {
        return logRecord;
    }

    public void setLogRecord(String logRecord) {
        this.logRecord = logRecord;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public String getLogRecordType() {
        return logRecordType;
    }

    public void setLogRecordType(String logRecordType) {
        this.logRecordType = logRecordType;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    @Override
    public String toString() {
        return "LogRecordMessage{" +
                "id='" + id + '\'' +
                ", bizTime=" + bizTime +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", sn='" + sn + '\'' +
                ", url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", bizId='" + bizId + '\'' +
                ", bizProd='" + bizProd + '\'' +
                ", bizAction='" + bizAction + '\'' +
                ", sysName='" + sysName + '\'' +
                ", bizIP='" + bizIP + '\'' +
                ", bizDetail='" + bizDetail + '\'' +
                ", costTime=" + costTime +
                ", responseCode='" + responseCode + '\'' +
                ", level='" + level + '\'' +
                ", traceId='" + traceId + '\'' +
                ", spanId='" + spanId + '\'' +
                ", tokenId='" + tokenId + '\'' +
                ", logRecord='" + logRecord + '\'' +
                ", stackTrace='" + stackTrace + '\'' +
                ", logRecordType='" + logRecordType + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", logType='" + logType + '\'' +
                '}';
    }
}
