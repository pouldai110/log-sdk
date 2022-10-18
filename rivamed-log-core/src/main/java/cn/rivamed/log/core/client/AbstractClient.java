package cn.rivamed.log.core.client;

import cn.rivamed.log.core.exception.LogQueueConnectException;
import redis.clients.jedis.JedisPubSub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public void pushMessage(String key, String strings) throws LogQueueConnectException {

    }

    public void putMessageList(String key, List<String> list) throws LogQueueConnectException {

    }
}
