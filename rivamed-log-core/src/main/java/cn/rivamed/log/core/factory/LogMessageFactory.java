package cn.rivamed.log.core.factory;

/**
 * className：logRecordMessageFactory
 * description： TODO
 * time：2022-10-03.14:04
 *
 * @author Zuo Yang
 * @version 1.0.0
 */
public class LogMessageFactory<T> {

    public static String packageMessage(String message, Object[] args) {
        StringBuilder builder = new StringBuilder(128);
        builder.append(message);
        for (Object arg : args) {
            builder.append("\n").append(arg);
        }
        return builder.toString();
    }

}
