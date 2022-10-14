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
public class LogRecordMessage extends BaseLogMessage {

    private static final long serialVersionUID = 1L;

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
     * 接口执行时长
     */
    private Long costTime;

    /**
     * 操作状态码
     */
    private String responseCode;

    /**
     * 登录tokenId
     */
    private String tokenId;

    /**
     * LogRecode(语义化日志字符串)
     */
    private String logRecord;

    /**
     * record日志类型 timeTaskLog：定时任务 userLog：接口
     */
    private String logRecordType;

    /**
     * 租户id
     */
    private String tenantId;

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
}
