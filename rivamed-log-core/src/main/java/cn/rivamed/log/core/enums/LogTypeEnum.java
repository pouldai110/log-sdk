package cn.rivamed.log.core.enums;

import cn.rivamed.log.core.constant.LogMessageConstant;
import cn.rivamed.log.core.entity.BaseLogMessage;

/**
 * 描述: TODO
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @author 左健宏
 * @version V2.0.1
 * @date 22/11/03 14:34
 */
public enum LogTypeEnum {

    LOG_TYPE_RECORD(LogMessageConstant.LOG_TYPE_RECORD),
    LOG_TYPE_RABBITMQ(LogMessageConstant.LOG_TYPE_RABBITMQ),
    LOG_TYPE_SYSTEM_LOG(LogMessageConstant.LOG_TYPE_SYSTEM_LOG),
    LOG_TYPE_LOGIN_LOG(LogMessageConstant.LOG_TYPE_LOGIN_LOG),
    LOG_TYPE_SCHEDULED_TASK_LOG(LogMessageConstant.LOG_TYPE_SCHEDULED_TASK_LOG),
    ;

    private String type;

    public String getType() {
        return type;
    }

    LogTypeEnum(String type) {
        this.type = type;
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
            String type = value.getType();
            if (formattedMessage.startsWith(type)) {
                logMessage.setBizDetail(formattedMessage.substring(type.length()));
                logMessage.setLogType(type);
                return logMessage;
            }
        }
        logMessage.setBizDetail(formattedMessage);
        logMessage.setLogType(LogMessageConstant.LOG_TYPE_SYSTEM_LOG);
        return logMessage;
    }

}
