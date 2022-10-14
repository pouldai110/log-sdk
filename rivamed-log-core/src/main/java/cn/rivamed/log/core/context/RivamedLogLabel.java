package cn.rivamed.log.core.context;

import java.io.Serializable;

/**
 * Rivamed Log Label上下文
 *
 * @author Zuo Yang
 * @since 1.0.0
 */
public class RivamedLogLabel implements Serializable {

    private static final long serialVersionUID = 5235299539593154309L;

    /**
     * 操作人账号
     */
    private String userId;

    /**
     * 操作人姓名
     */
    private String userName;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 设备sn
     */
    private String sn;

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
     * 操作状态码
     */
    private String responseCode;

    /**
     * LogRecode(语义化日志字符串)
     */
    private String logRecord;

    /**
     * 登录tokenId
     */
    private String tokenId;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 系统名称
     */
    private String sysName;

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

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getLogRecord() {
        return logRecord;
    }

    public void setLogRecord(String logRecord) {
        this.logRecord = logRecord;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }
}
