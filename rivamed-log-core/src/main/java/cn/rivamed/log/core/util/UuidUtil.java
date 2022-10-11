package cn.rivamed.log.core.util;

/**
 * @author Zuo Yang
 */

import java.util.UUID;

/**
 * @ClassName: UuidUtil
 * @Description: (uuid工具类)
 * @author Zuo Yang
 * @date 2020年6月28日 下午2:13:04
 *
 */
public class UuidUtil {

    /** 中划线 */
    public static final String LINE_THROUGH = "-";

    /** 空字符串 */
    public static final String NULL_STRING = "";

    /**
     * @author Zuo Yang
     * @Title: generatorUuid
     * @Description: (获取uuid)
     * @param
     * @return String 返回类型
     * @throws
     */
    public static String generatorUuid() {
        String uuid = UUID.randomUUID().toString(); // 转化为String对象
        uuid = uuid.replace(LINE_THROUGH, NULL_STRING); // 因为UUID本身为32位只是生成时多了“-”，所以将它们去点就可
        return uuid;
    }

}
