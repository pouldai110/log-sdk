package cn.rivamed.log.core.client;

import java.util.List;


/**
 * className：AbstractClient
 * description： 日志抽象客户端
 * time：2022-10-03.11:47
 *
 * @author Zuo Yang
 * @version 1.0.0
 */
public abstract class AbstractClient {

    private static AbstractClient client;

    public static AbstractClient getClient() {
        return client;
    }

    public static void setClient(AbstractClient abstractClient) {
        client = abstractClient;
    }

    public void pushMessage(String strings) {

    }

    public void pushSimpleMessage(String queueName, Object object) {

    }

    public void putMessageList(List<String> list) {

    }
}
