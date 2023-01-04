package cn.rivamed.log.core.enums;

import cn.rivamed.log.core.constant.LogMessageConstant;

/**
 * 描述: 日志MQ相关队列
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @author 左健宏
 * @version V2.0.1
 * @date 22/11/03 14:34
 */
public enum RivamedLogQueueEnum {

    RIVAMED_BUSINESS_LOG_QUEUE(LogMessageConstant.RIVAMED_LOG_CLIENT_EXCHANGE_NAME, LogMessageConstant.RIVAMED_BUSINESS_LOG_ROUTING_KEY_NAME, LogMessageConstant.RIVAMED_BUSINESS_LOG_QUEUE_NAME),
    RIVAMED_LOGIN_LOG_QUEUE(LogMessageConstant.RIVAMED_LOG_CLIENT_EXCHANGE_NAME, LogMessageConstant.RIVAMED_LOGIN_LOG_ROUTING_KEY_NAME, LogMessageConstant.RIVAMED_LOGIN_LOG_QUEUE_NAME),
    RIVAMED_REG_LOG_QUEUE(LogMessageConstant.RIVAMED_LOG_CLIENT_EXCHANGE_NAME, LogMessageConstant.RIVAMED_REG_LOG_ROUTING_KEY_NAME, LogMessageConstant.RIVAMED_REG_LOG_QUEUE_NAME),
    ;

    private String exchangeName;

    private String routingKey;

    private String queueName;

    public String getExchangeName() {
        return exchangeName;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public String getQueueName() {
        return queueName;
    }

    RivamedLogQueueEnum(String exchangeName, String routingKey, String queueName) {
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
        this.queueName = queueName;
    }}
