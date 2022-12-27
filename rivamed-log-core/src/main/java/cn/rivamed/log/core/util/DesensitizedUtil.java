package cn.rivamed.log.core.util;

import cn.rivamed.log.core.enums.RivamedLogDesensitizedEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>描述：脱敏工具类</p>
 * <p>公司：浙江瑞华康源科技有限公司</p>
 * <p>版权：rivamed-2021</p>
 *
 * @author daiyaopeng
 * @since 2022/12/21
 */
public class DesensitizedUtil {
    public DesensitizedUtil() {
    }

    public static String desensitized(CharSequence str, DesensitizedUtil.DesensitizedType desensitizedType) {
        if (StringUtils.isBlank(str)) {
            return "";
        } else {
            String newStr;
            switch (desensitizedType) {
                case USER_ID:
                    newStr = String.valueOf(userId());
                    break;
                case CHINESE_NAME:
                    newStr = chineseName(String.valueOf(str));
                    break;
                case ID_CARD:
                    newStr = idCardNum(String.valueOf(str), 4, 4);
                    break;
                case FIXED_PHONE:
                    newStr = fixedPhone(String.valueOf(str));
                    break;
                case MOBILE_PHONE:
                    newStr = mobilePhone(String.valueOf(str));
                    break;
                case ADDRESS:
                    newStr = address(String.valueOf(str), 8);
                    break;
                case EMAIL:
                    newStr = email(String.valueOf(str));
                    break;
                case PASSWORD:
                    newStr = password(String.valueOf(str));
                    break;
                case CAR_LICENSE:
                    newStr = carLicense(String.valueOf(str));
                    break;
                case BANK_CARD:
                    newStr = bankCard(String.valueOf(str));
                    break;
                default:
                    newStr = defaultStr(String.valueOf(str));
                    break;
            }

            return newStr;
        }
    }

    public static Long userId() {
        return 0L;
    }

    /**
     * <p>功能描述: 字符串默认脱敏</p>
     *
     * @param fullName
     * @return java.lang.String
     * @author daiyaopeng
     * @date 2022/12/21
     */
    public static String defaultStr(String fullName) {
        return StringUtils.isBlank(fullName) ? "" : replaceStr(fullName, 2, fullName.length() - 2);
    }

    /**
     * <p>功能描述: 中文脱敏</p>
     *
     * @param fullName
     * @return java.lang.String
     * @author daiyaopeng
     * @date 2022/12/21
     */
    public static String chineseName(String fullName) {
        return StringUtils.isBlank(fullName) ? "" : replaceStr(fullName, 1, fullName.length());
    }

    /**
     * 身份证号码脱敏
     *
     * @param idCardNum 身份证号
     * @param front     开始保留长度
     * @param end       结尾保留长度
     * @return
     */
    public static String idCardNum(String idCardNum, int front, int end) {
        if (StringUtils.isBlank(idCardNum)) {
            return "";
        } else if (front + end > idCardNum.length()) {
            return "";
        } else {
            return front >= 0 && end >= 0 ? replaceStr(idCardNum, front, idCardNum.length() - end) : "";
        }
    }

    /**
     * <p>功能描述: 固定电话脱敏</p>
     *
     * @param num
     * @return java.lang.String
     * @author daiyaopeng
     * @date 2022/12/21
     */
    public static String fixedPhone(String num) {
        return StringUtils.isBlank(num) ? "" : replaceStr(num, 4, num.length() - 2);
    }

    /**
     * <p>功能描述: 手机电话脱敏</p>
     *
     * @param num
     * @return java.lang.String
     * @author daiyaopeng
     * @date 2022/12/21
     */
    public static String mobilePhone(String num) {
        return StringUtils.isBlank(num) ? "" : replaceStr(num, 3, num.length() - 4);
    }

    /**
     * <p>功能描述: 地址脱敏</p>
     *
     * @param address       地址信息
     * @param sensitiveSize 不脱敏数据长度
     * @return java.lang.String
     * @author daiyaopeng
     * @date 2022/12/21
     */
    public static String address(String address, int sensitiveSize) {
        if (StringUtils.isBlank(address)) {
            return "";
        } else {
            int length = address.length();
            return replaceStr(address, length - sensitiveSize, length);
        }
    }

    /**
     * <p>功能描述: email 脱敏</p>
     *
     * @param email
     * @return java.lang.String
     * @author daiyaopeng
     * @date 2022/12/21
     */
    public static String email(String email) {
        if (StringUtils.isBlank(email)) {
            return "";
        } else {
            int index = StringUtils.indexOf(email, '@');
            return index <= 1 ? email : replaceStr(email, 1, index);
        }
    }

    /**
     * <p>功能描述: 密码脱敏</p>
     *
     * @param password
     * @return java.lang.String
     * @author daiyaopeng
     * @date 2022/12/21
     */
    public static String password(String password) {
        return StringUtils.isBlank(password) ? "" : StringUtils.repeat('*', password.length());
    }

    /**
     * <p>功能描述: 其他id卡脱敏</p>
     *
     * @param carLicense
     * @return java.lang.String
     * @author daiyaopeng
     * @date 2022/12/21
     */
    public static String carLicense(String carLicense) {
        if (StringUtils.isBlank(carLicense)) {
            return "";
        } else {
            if (carLicense.length() == 7) {
                carLicense = replaceStr(carLicense, 3, 6);
            } else if (carLicense.length() == 8) {
                carLicense = replaceStr(carLicense, 3, 7);
            }

            return carLicense;
        }
    }

    /**
     * <p>功能描述: 银行卡号脱敏</p>
     *
     * @param bankCardNo 银行卡号
     * @return java.lang.String
     * @author daiyaopeng
     * @date 2022/12/21
     */
    public static String bankCard(String bankCardNo) {
        if (StringUtils.isBlank(bankCardNo)) {
            return bankCardNo;
        } else {
            bankCardNo = StringUtils.trim(bankCardNo);
            if (bankCardNo.length() < 9) {
                return bankCardNo;
            } else {
                int length = bankCardNo.length();
                int midLength = length - 8;
                StringBuilder buf = new StringBuilder();
                buf.append(bankCardNo, 0, 4);

                for (int i = 0; i < midLength; ++i) {
                    if (i % 4 == 0) {
                        buf.append(' ');
                    }

                    buf.append('*');
                }

                buf.append(' ').append(bankCardNo, length - 4, length);
                return buf.toString();
            }
        }
    }

    /**
     * 字符串替换指定长度字符为 “*”
     *
     * @param str
     * @param startInclude
     * @param endExclude
     * @return
     */
    private static String replaceStr(String str, int startInclude, int endExclude) {
        char replacedChar = '*';
        if (StringUtils.isEmpty(str)) {
            return str(str);
        } else {
            int strLength = str.length();
            if (startInclude > strLength) {
                return str(str);
            } else {
                if (endExclude > strLength) {
                    endExclude = strLength;
                }

                if (startInclude > endExclude) {
                    return str(str);
                } else {
                    char[] chars = new char[strLength];

                    for (int i = 0; i < strLength; ++i) {
                        if (i >= startInclude && i < endExclude) {
                            chars[i] = replacedChar;
                        } else {
                            chars[i] = str.charAt(i);
                        }
                    }

                    return new String(chars);
                }
            }
        }
    }

    /**
     * <p>功能描述: json脱敏默认数据</p>
     *
     * @param data
     * @return java.lang.String
     * @author daiyaopeng
     * @date 2022/12/21
     */
    public static String desensitizedJsonData(String data) {
        if (StringUtils.isBlank(data)) {
            return data;
        }
        String regexParamStr = "\"%s\":\".*?\"";
        String regexValueStr = "\"%s\":\"%m\"";
        for (RivamedLogDesensitizedEnum desensitizedEnum : RivamedLogDesensitizedEnum.values()) {
            String replace = regexParamStr.replace("%s", desensitizedEnum.getParam());
            if (data.matches(replace)) {
                String replaceValue = regexValueStr.replace("%s", desensitizedEnum.getParam()).replace("%m", desensitizedEnum.getDesensitizedValue());
                data = data.replaceAll(replace, replaceValue);
            }
        }
        return data;
    }

    public static String str(CharSequence cs) {
        return null == cs ? null : cs.toString();
    }

    public static enum DesensitizedType {
        USER_ID,
        CHINESE_NAME,
        ID_CARD,
        FIXED_PHONE,
        MOBILE_PHONE,
        ADDRESS,
        EMAIL,
        PASSWORD,
        CAR_LICENSE,
        BANK_CARD;

        private DesensitizedType() {
        }
    }
}
