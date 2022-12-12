package cn.rivamed.log.core.enums;

import cn.rivamed.log.core.constant.LogMessageConstant;

/**
 * 描述: MQ消息类型
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @author 左健宏
 * @version V2.0.1
 * @date 22/11/03 14:34
 */
public enum MQTypeEnum {

    MESSAGE_TYPE_SEND("1", LogMessageConstant.MESSAGE_TYPE_SEND),
    MESSAGE_TYPE_ACCEPT("2", LogMessageConstant.MESSAGE_TYPE_ACCEPT),
    ;

    private String type;

    private String desc;

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    MQTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

}
