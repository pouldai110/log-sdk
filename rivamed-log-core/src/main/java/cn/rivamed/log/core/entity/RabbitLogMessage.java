package cn.rivamed.log.core.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RabbitLogMessage extends BaseLogMessage {

    private static final long serialVersionUID = 1L;

    /**
     * 类型 send:发送 accept:接收
     */
    private String mqType;

    /**
     * 队列IP地址
     */
    private String rabbitMQHost;

    /**
     * 队列端口号
     */
    private Integer rabbitMQPort;

    /**
     * vhost
     */
    private String vhost;

    /**
     * 交换机
     */
    private String exchange;

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
     * 消息内容
     */
    private Object message;


    public RabbitLogMessage(String exchange, String routingKey, Object message) {
        this.exchange = exchange;
        this.routingKey = routingKey;
        this.message = message;
    }

    public RabbitLogMessage(String exchange, String routingKey, String queueName, Object message) {
        this.exchange = exchange;
        this.routingKey = routingKey;
        this.queueName = queueName;
        this.message = message;
    }

}
