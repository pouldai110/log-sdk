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
 * description： TODO
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

    public List<String> getMessage(String key, int size) throws LogQueueConnectException {
        List<String> list = new ArrayList<>();
        return list;
    }

    public boolean setNx(String key, Integer expire) {
        return false;
    }

    public boolean existsKey(String key) {
        return true;

    }

    public String getMessage(String key) {
        return null;
    }

    public void set(String key, String value) {
    }

    public void set(String key, String value, int seconds) {
    }

    public void expireAt(String key, Long time) {
    }

    public void expire(String key, int seconds) {
    }

    public Long incr(String key) {
        Long re = 0L;
        return re;
    }

    public Long incrBy(String key, int value) {
        Long re = 0L;
        return re;
    }

    public void hset(String key, Map<String, String> value) {
    }

    public void sadd(String key, String value) {
    }

    public Set<String> smembers(String key) {
        return new HashSet<>();
    }

    public void del(String key) {
    }

    public void hset(String key, String field, String value) {
    }

    public void hdel(String key, String... field) {
    }

    public String hget(String key, String field) {
        String value = "";
        return value;
    }

    public Long llen(String key) {
        Long value = 0L;
        return value;
    }

    public Map<String, String> hgetAll(String key) {
        Map<String, String> value = new HashMap<>();
        return value;
    }

    public List<String> hmget(String key, String... field) {
        return new ArrayList<>();
    }

    public Long hincrby(String key, String field, int num) {
        return null;
    }

    public void publish(String channel, String message) {
    }

    public Long hlen(String key) {
        return null;
    }

    public void subscribe(JedisPubSub jedisPubSub, String... channel) {
    }

}
