package org.xiaoxi.cache.client;

import redis.clients.jedis.*;

/**
 * Created by YanYang on 2016/6/18.
 */
public class CacheClient {
    private static JedisPool jedisPool; //非切片连接池

    static {
        init();
    }

    /**
     * 初始化非切片池
     */
    private static void init() {
        //池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(5);
        config.setMaxWaitMillis(10001);
        config.setTestOnBorrow(false);

        jedisPool = new JedisPool(config, "127.0.0.1", 6379);
    }

    public static String get(String key) throws Exception{
        try (Jedis jedis = jedisPool.getResource()) {
            String value = jedis.get(key);
            return value;
        }
    }

    public static void set(String key, String value) throws Exception{
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
        }
    }
}
