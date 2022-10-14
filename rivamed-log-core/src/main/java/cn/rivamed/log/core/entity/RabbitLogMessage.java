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
public class RabbitLogMessage extends BaseLogMessage {

    private static final long serialVersionUID = 1L;

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
}
