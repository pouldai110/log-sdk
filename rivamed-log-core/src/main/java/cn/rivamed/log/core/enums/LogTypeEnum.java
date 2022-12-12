package cn.rivamed.log.core.enums;

import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.entity.BaseLogMessage;

/**
 * 描述: 日志类型
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @author 左健宏
 * @version V2.0.1
 * @date 22/11/03 14:34
 */
public enum LogTypeEnum {

    LOG_TYPE_RECORD_LOG("1", LogMessageConstant.LOG_TYPE_RECORD_LOG),
    LOG_TYPE_RABBITMQ_LOG("2", LogMessageConstant.LOG_TYPE_RABBITMQ_LOG),
    LOG_TYPE_SYSTEM_LOG("3", LogMessageConstant.LOG_TYPE_SYSTEM_LOG),
    LOG_TYPE_SCHEDULED_TASK_LOG("4", LogMessageConstant.LOG_TYPE_SCHEDULED_TASK_LOG),
    LOG_TYPE_LOGIN_LOG("5", LogMessageConstant.LOG_TYPE_LOGIN_LOG),
    ;

    private String type;

    private String desc;

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    LogTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    /**
     * 根据日志数据获得类型并转换
     *
     * @param formattedMessage
     * @return
     */
    public static BaseLogMessage convertMessageType(String formattedMessage) {
        BaseLogMessage logMessage = new BaseLogMessage();
        for (LogTypeEnum value : LogTypeEnum.values()) {
            String desc = value.getDesc();
            if (formattedMessage.startsWith(desc)) {
                logMessage.setBizDetail(formattedMessage.substring(desc.length()));
                logMessage.setLogType(value.getType());
                return logMessage;
            }
        }
        logMessage.setBizDetail(formattedMessage);
        logMessage.setLogType(LOG_TYPE_SYSTEM_LOG.getType());
        return logMessage;
    }

}
