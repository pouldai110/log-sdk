package cn.rivamed.log.core.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述: 队列日志
 * 公司: 北京瑞华康源科技有限公司<br/>
 * 版权: rivamed2020<br/>
 *
 * @author fujiali
 * @version V1.0
 * @date 2022/9/27 15:30
 */
public class RabbitLogMessage implements Serializable {

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
     * 执行机器ip
     */
    private String bizIP;

    /**
     * traceId
     */
    private String traceId;

    /**
     * spanId
     */
    private String spanId;

    /**
     * 类型 send:发送 accept:接收
     */
    private String mqType;

    /**
     * vhost
     */
    private String vhost;

    /**
     * 交换机
     */
    private String exchangeKey;

    /**
     * 路由
     */
    private String routingKey;

    /**
     * 队列
     */
    private String queueName;

    /**
     * 消息id
     */
    private String messageId;

    /**
     * 租户id
     */
    private String tenantId;

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

    public String getBizIP() {
        return bizIP;
    }

    public void setBizIP(String bizIP) {
        this.bizIP = bizIP;
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

    public String getMqType() {
        return mqType;
    }

    public void setMqType(String mqType) {
        this.mqType = mqType;
    }

    public String getVhost() {
        return vhost;
    }

    public void setVhost(String vhost) {
        this.vhost = vhost;
    }

    public String getExchangeKey() {
        return exchangeKey;
    }

    public void setExchangeKey(String exchangeKey) {
        this.exchangeKey = exchangeKey;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
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

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    @Override
    public String toString() {
        return "RabbitLogMessage{" +
                "id='" + id + '\'' +
                ", bizTime=" + bizTime +
                ", bizIP='" + bizIP + '\'' +
                ", traceId='" + traceId + '\'' +
                ", spanId='" + spanId + '\'' +
                ", mqType='" + mqType + '\'' +
                ", vhost='" + vhost + '\'' +
                ", exchangeKey='" + exchangeKey + '\'' +
                ", routingKey='" + routingKey + '\'' +
                ", queueName='" + queueName + '\'' +
                ", messageId='" + messageId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", logType='" + logType + '\'' +
                ", sysName='" + sysName + '\'' +
                '}';
    }
}
