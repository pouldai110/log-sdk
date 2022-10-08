package cn.rivamed.log.core.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * 描述: 队列日志
 * 公司: 北京瑞华康源科技有限公司<br/>
 * 版权: rivamed2020<br/>
 *
 * @author fujiali
 * @date 2022/9/27 15:30
 * @version V1.0
 */
@Data
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

}
