package cn.rivamed.log.core.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 描述: 队列日志
 * 公司: 北京瑞华康源科技有限公司<br/>
 * 版权: rivamed2020<br/>
 *
 * @author fujiali
 * @version V1.0
 * @date 2022/9/27 15:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
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

}
