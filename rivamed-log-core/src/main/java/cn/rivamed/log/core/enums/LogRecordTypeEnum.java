package cn.rivamed.log.core.enums;

import cn.rivamed.log.core.constant.LogMessageConstant;

/**
 * 描述: 语义化日志类型
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @author 左健宏
 * @version V2.0.1
 * @date 22/11/03 14:34
 */
public enum LogRecordTypeEnum {

    LOG_RECORD_TYPE_SYSTEM("1", LogMessageConstant.LOG_RECORD_TYPE_SYSTEM),
    LOG_RECORD_TYPE_SWAGGER("2", LogMessageConstant.LOG_RECORD_TYPE_SWAGGER),
    LOG_RECORD_TYPE_MZTBIZ("3", LogMessageConstant.LOG_RECORD_TYPE_MZTBIZ)
    ;

    private String type;

    private String desc;

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    LogRecordTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

}
