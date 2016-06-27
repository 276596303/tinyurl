package org.xiaoxi.rateLimiter.dao;

import com.sun.org.glassfish.gmbal.ParameterNames;
import com.sun.scenario.effect.impl.prism.PrImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YanYang on 2016/6/24.
 */
public final class RedisClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisClient.class);

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

    //设置 key-value （永久存活）
    public static void set(String key, String value) {
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

    // 设置 key-value（有存活时间）
    public static void set(String key, String value, int second) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.setex(key, second, value);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            jedis.close();
        }
    }

    // 单次获取（有默认值）
    public static String get(String key, String defaultValue) {
        Jedis jedis = null;
        String value = defaultValue;
        try {
            jedis = jedisPool.getResource();
            String temp = jedis.get(key);
            if (temp != null) {
                value = temp;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            jedis.close();
        }
        return value;
    }

    // 把key对应的value 加 count
    public static void incrBy(String key, long count) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.incrBy(key, count);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            jedis.close();
        }
    }

    //批量获取
    public static List<String> pipeGet(List<String> keys, String defaultValue) {
        Jedis jedis = null;
        Pipeline pipeline = null;
        List<Object> pipelineRes = null;   //批量获取的结果集（Object 可以是任意对象 字符、集合等）
        List<String> result = new ArrayList<String>();  // 最终要返回的结果集
        try {
            jedis = jedisPool.getResource();
            pipeline = jedis.pipelined();    // 获取管道

            for (String key : keys) {
                pipeline.get(key);          // 向管道中加入操作集
            }
            pipelineRes = pipeline.syncAndReturnAll();   // 把所有操作发送出去， 并获得结果集

            // 将结果集 转化为 合适的结果集
            for (Object object : pipelineRes) {
                if (object != null) {
                    result.add((String) object);
                } else {
                    result.add(defaultValue);
                }
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            jedis.close();
        }

        return result;
    }
}
