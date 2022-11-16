package cn.rivamed.log.core.util;

/**
 * 描述: 消息模板
 * 公司 北京瑞华康源科技有限公司
 * 版本 Rivamed 2022
 *
 * @author 左健宏
 * @version V2.0.1
 * @date 22/11/14 10:33
 */
public class LogTemplateUtil {

    public static final String TASK_SUCCESS_FORMAT = " 在 %s 自动执行了 %s 方法，耗时%s 毫秒";

    public static final String TASK_FAIL_FORMAT = " 在 %s 自动执行 %s 方法失败";

    public static final String LOG_RECORD_SUCCESS_FORMAT = " 执行了 %s 方法，耗时%s 毫秒";

    public static final String LOG_RECORD_FAIL_FORMAT = " 执行 %s 方法失败";

}
