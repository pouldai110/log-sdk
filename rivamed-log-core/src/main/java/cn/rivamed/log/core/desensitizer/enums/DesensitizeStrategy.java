package cn.rivamed.log.core.desensitizer.enums;

import cn.rivamed.log.core.util.DesensitizedUtil;

import java.util.function.Function;

/**
 * 描述: jackson脱敏策略
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @version V2.10.0
 * @author: 左健宏
 * @date: 2022/12/26 10:48
 */
public enum DesensitizeStrategy {

    /**
     * 对字符串进行脱敏
     */
    DEFAULT(s -> DesensitizedUtil.defaultStr(s)),

    /**
     * 对用户名进行脱敏
     */
    CHINESE_NAME(s -> DesensitizedUtil.chineseName(s)),

    /**
     * 对身份证进行脱敏
     */
    ID_CARD(s -> DesensitizedUtil.idCardNum(s, 4, 4)),

    /**
     * 对座机号码进行脱敏
     */
    FIXED_PHONE(s -> DesensitizedUtil.fixedPhone(s)),


    /**
     * 对电话号码进行脱敏
     */
    MOBILE_PHONE(s -> DesensitizedUtil.mobilePhone(s)),

    /**
     * 对邮箱进行脱敏
     */
    EMAIL(s -> DesensitizedUtil.email(s)),

    /**
     * 对地址进行脱敏
     */
    ADDRESS(s -> DesensitizedUtil.address(s, 8)),

    /**
     * 对车牌号进行脱敏
     */
    CAR_LICENSE(s -> DesensitizedUtil.carLicense(s)),

    /**
     * 对银行卡号进行脱敏
     */
    BANK_CARD(s -> DesensitizedUtil.bankCard(s)),

    /**
     * 对密码进行脱敏，全部加密即可
     */
    PASSWORD(s -> "********");


    private final Function<String, String> desensitizeSerializer;

    DesensitizeStrategy(Function<String, String> desensitizeSerializer) {
        this.desensitizeSerializer = desensitizeSerializer;
    }

    // 用于后续获取脱敏的规则，实现脱敏
    public Function<String, String> desensitizeSerializer() {
        return desensitizeSerializer;
    }

}
