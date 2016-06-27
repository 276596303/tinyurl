package org.xiaoxi.cache.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

/**
 * Created by YanYang on 2016/6/18.
 */
public class CacheClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheClient.class);

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
        Jedis jedis = null;
        String value = null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            jedis.close();
        }
        return value;
    }

    public static void set(String key, String value) throws Exception{
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            jedis.close();
        }
    }
}
